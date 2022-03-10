package com.gapahce.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.AuthConstants;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.security.checker.AsyncSecurityChecker;
import com.gapache.security.checker.SignChecker;
import com.gapache.security.model.AccessCard;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.gapache.commons.model.AuthConstants.TOKEN_HEADER;

/**
 *
 * @author HuSen
 * @since 2020/8/10 10:20 上午
 */
@Slf4j
@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    private final AsyncSecurityChecker asyncSecurityChecker;
    private final SignChecker signChecker;

    public SecurityFilter(AsyncSecurityChecker asyncSecurityChecker, SignChecker signChecker) {
        this.asyncSecurityChecker = asyncSecurityChecker;
        this.signChecker = signChecker;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(TOKEN_HEADER);
        // 并不是所有的请求都需要token吗
        if (StringUtils.isBlank(token)) {
            return chain.filter(exchange);
        }

        return Mono.create(ms -> asyncSecurityChecker.checking(token)
                .onSuccess(accessCard -> {
                    if (accessCard != null) {
                        byte[] message = ProtocstuffUtils.bean2Byte(accessCard, AccessCard.class);
                        if (message != null) {
                            String value = Base64Utils.encodeToString(message);
                            log.info("{} :{}", AuthConstants.ACCESS_CARD_HEADER, value);
                            exchange.getRequest().mutate()
                                    .headers(httpHeaders -> httpHeaders.add(AuthConstants.ACCESS_CARD_HEADER, value));
                        }
                        // 如果解析出了access card，就进行签名验证
                        // 不必担心请求不传token的情况，因为需要签名验证的接口，一定是需要access card的，所以不传也会被资源服务器自己所拦截
                        if (accessCard.getSign()) {
                            String body = resolveBodyFromRequest(exchange.getRequest());
                            if (StringUtils.isNotBlank(body)) {
                                if (!signChecker.checkSign(JSON.parseObject(body), accessCard.getClientId())) {
                                    ms.error(new IllegalAccessError("sign error"));
                                }
                            }
                        }
                    }

                    chain.filter(exchange)
                            .doFinally(t -> ms.success())
                            .subscribe(ms::success);
                })
                .onFailure(error -> {
                    log.error(">>>>>>", error);
                    ms.error(error);
                }));
    }

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        StringBuilder sb = new StringBuilder();

        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            sb.append(bodyString);
        });
        return sb.toString();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
