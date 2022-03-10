package com.gapache.vertx.web.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 请求帮助工具，返回
 * Future<AsyncResult<HttpClientResponse>>
 *
 * @author HuSen
 * @since 2021/3/2 1:36 下午
 */
public class HttpRequestHelper {

    @SuppressWarnings("unchecked")
    public static Future<AsyncResult<HttpClientResponse>> call(HttpClient client, HttpMethod method, String host, int port, String uri, String body, Map<String, Object> headers) {
        return Future.future(promise -> client.request(method, port, host, uri, asyncResult -> {
            if (asyncResult.succeeded()) {
                HttpClientRequest request = asyncResult.result();

                if (headers != null && !headers.isEmpty()) {
                    headers.forEach((name, value) -> {
                        if (value instanceof String) {
                            request.putHeader(name, (String) value);
                        } else if (value instanceof Iterable) {
                            request.putHeader(name, (Iterable<String>) value);
                        } else {
                            request.putHeader(name, value.toString());
                        }
                    });
                }

                request.putHeader("content-type", "application/json");

                if (StringUtils.hasText(body)) {
                    request.send(body, promise::complete);
                } else {
                    request.send(promise::complete);
                }
            } else {
                promise.fail(asyncResult.cause());
            }
        }));
    }
}
