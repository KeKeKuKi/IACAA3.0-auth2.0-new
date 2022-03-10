package com.gapache.vertx.redis.annotation;

import java.lang.annotation.*;

/**
 * Redis Entity Id
 *
 * @author HuSen
 * @since 2021/3/8 11:04 上午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
}
