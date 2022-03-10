package com.gapache.vertx.web.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2021/3/1 3:42 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface VertxController {
}
