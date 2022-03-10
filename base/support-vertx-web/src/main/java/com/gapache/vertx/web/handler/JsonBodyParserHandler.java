package com.gapache.vertx.web.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 解析json body的处理器
 *
 * @author HuSen
 * @since 2021/3/1 6:40 下午
 */
@Slf4j
public class JsonBodyParserHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        String body = routingContext.getBodyAsString();

        if (StringUtils.hasText(body)) {
            try {
                routingContext.put("jsonObject", JSON.parseObject(body));
            } catch (Exception ignored) {}

            try {
                routingContext.put("jsonArray", JSONArray.parseArray(body));
            } catch (Exception ignored) {}
        }

        routingContext.next();
    }
}
