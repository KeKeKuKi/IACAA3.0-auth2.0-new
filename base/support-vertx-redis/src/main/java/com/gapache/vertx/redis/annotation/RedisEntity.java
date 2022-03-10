package com.gapache.vertx.redis.annotation;

import java.lang.annotation.*;

/**
 * Redis Entity
 *
 * @author HuSen
 * @since 2021/3/8 10:51 上午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RedisEntity {

    /**
     * 相当于表名
     * 如果为空，等于ClassName
     *
     * @return 表名
     */
    String value() default "";
}
