package com.gapache.vertx.web.server;

import com.gapache.vertx.core.VertxManager;
import com.gapache.vertx.core.VertxSettings;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * @author HuSen
 * @since 2021/3/1 3:16 下午
 */
public class HttpServerVerticle extends AbstractVerticle {

    private final VertxSettings.HttpServer settings;
    private HttpServer server;
    private final Router router;

    public HttpServerVerticle(VertxSettings.HttpServer settings, Router router) {
        this.settings = settings;
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        server = VertxManager.checkNewStandalone().createHttpServer();

        server.requestHandler(router).listen(settings.getPort(), res -> {
            if (res.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(res.cause());
            }
        });
    }

    @Override
    public void stop() {
        server.close().onComplete(as -> {
            // TODO
        });
    }
}
