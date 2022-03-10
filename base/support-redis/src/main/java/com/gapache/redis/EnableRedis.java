package com.gapache.redis;

import java.lang.annotation.*;

/**
 * @author HuSen
 * create on 2020/1/15 11:52
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableRedis {

    boolean enableTransactionSupport() default false;
}
