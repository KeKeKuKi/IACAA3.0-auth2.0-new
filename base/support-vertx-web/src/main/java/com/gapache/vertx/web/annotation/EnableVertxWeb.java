package com.gapache.vertx.web.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2021/3/3 1:40 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableVertxWeb {
}
