package com.gapache.vertx.core;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author HuSen
 * @since 2021/3/1 2:55 下午
 */
@Slf4j
public class VertxManager {

    private Vertx vertx;
    private volatile static Vertx standaloneVertx;
    private VertxSettings settings;
    private static VertxManager instance;

    public void createCluster(VertxSettings settings, ApplicationContext applicationContext) {
        // TODO apply settings
        this.settings = settings;
        VertxOptions options = new VertxOptions();

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                log.info("create hazelcast cluster success");
                this.vertx = res.result();
                VertxManager.instance = this;
                applicationContext.publishEvent(new VertxCreatedEvent(new Object(), true));

                // check
                EventBus eventBus = this.vertx.eventBus();
                eventBus.consumer("vertx.health.check.address", (Handler<Message<String>>) event -> log.info("check:{}", event.body()));
            } else {
                // failed!
                log.error("create hazelcast cluster fail", res.cause());
                applicationContext.publishEvent(new VertxCreatedEvent(new Object(), false));
            }
        });
    }

    public void createStandalone(VertxSettings settings, ApplicationContext applicationContext) {
        this.settings = settings;
        this.vertx = Vertx.vertx();
        VertxManager.instance = this;
        applicationContext.publishEvent(new VertxCreatedEvent(new Object(), true));
        // check
        EventBus eventBus = this.vertx.eventBus();
        eventBus.consumer("vertx.health.check.address", (Handler<Message<String>>) event -> log.info("check:{}", event.body()));
    }

    public static Vertx getVertx() {
        if (instance == null) {
            return checkNewStandalone();
        }
        return instance.vertx;
    }

    public static Vertx checkNewStandalone() {
        if (standaloneVertx == null) {
            synchronized (VertxManager.class) {
                if (standaloneVertx == null) {
                    log.warn("create a new default vertx");
                    standaloneVertx = Vertx.vertx();
                }
            }
        }
        return standaloneVertx;
    }

    public static VertxSettings getSettings() {
        return instance.settings;
    }
}
