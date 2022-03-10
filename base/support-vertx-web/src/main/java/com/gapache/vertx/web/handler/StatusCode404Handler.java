package com.gapache.vertx.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author HuSen
 * @since 2021/3/2 10:04 上午
 */
@Slf4j
public class StatusCode404Handler implements Handler<RoutingContext> {

    private final Map<String, Set<String>> routeCache;

    public StatusCode404Handler() {
        routeCache = new HashMap<>(256);
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String path = routingContext.request().path();
        String method = routingContext.request().method().name();
        if (!routeCache.containsKey(path) || !routeCache.get(path).contains(method)) {
            routingContext.response().setStatusCode(404).end(path + " resource is not found!");
        } else {
            routingContext.next();
        }
    }

    public void cache(String path, String method) {
        Set<String> methods = routeCache.computeIfAbsent(path, key -> new HashSet<>(32));
        methods.add(method);
    }
}
