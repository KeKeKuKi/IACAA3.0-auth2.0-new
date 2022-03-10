package com.gapache.vertx.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 如果这样写就必须是非阻塞的方法才行
 *
 * @author HuSen
 * @since 2021/3/1 7:04 下午
 */
@Slf4j
public class MethodInvokeHandler implements Handler<RoutingContext> {

    private final Method method;
    private final Object target;

    public MethodInvokeHandler(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        Object[] args = routingContext.get("args");
        Object returnObj = null;
        try {
            if (args != null) {
                returnObj = method.invoke(target, args);
            } else {
                returnObj = method.invoke(target);
            }
        } catch (Exception e) {
            log.error("", e);
        }

        if (returnObj != null) {
            routingContext.put("returnObj", returnObj);
        }
        routingContext.next();
    }
}
