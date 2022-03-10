package com.gapache.vertx.core;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author HuSen
 * @since 2021/3/5 9:55 上午
 */
@Getter
public class VertxCreatedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2762206369427431037L;

    private final boolean success;

    public VertxCreatedEvent(Object source, boolean success) {
        super(source);
        this.success = success;
    }
}
