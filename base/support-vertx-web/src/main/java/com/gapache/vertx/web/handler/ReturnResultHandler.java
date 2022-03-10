package com.gapache.vertx.web.handler;

import com.alibaba.fastjson.JSON;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * @since 2021/3/1 7:03 下午
 */
@Slf4j
public class ReturnResultHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        Object returnObj = routingContext.get("returnObj");
        if (returnObj == null) {
            routingContext.response().putHeader("content-type", "application/json").end();
        } else {
            routingContext.response().putHeader("content-type", "application/json");
            if (returnObj instanceof Future) {
                ((Future<?>) returnObj).onComplete(as -> {
                    if (as.succeeded()) {
                        routingContext.response().end(JSON.toJSONString(as.result()), logger);
                    } else {
                        routingContext.response().end(as.cause().toString(), logger);
                    }
                });
            } else {
                routingContext.response().putHeader("content-type", "application/json").end(JSON.toJSONString(returnObj), logger);
            }
        }
    }

    private final Handler<AsyncResult<Void>> logger = asyncResult -> {
        if (log.isDebugEnabled()) {
            log.debug(">>>>>> return result: {}", asyncResult.succeeded());
        }
    };
}
