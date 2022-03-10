package com.gapache.vertx.web.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 为此接口生成一个动态代理的类
 *
 * @author HuSen
 * @since 2021/3/2 1:32 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ZeusClient {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean decode404() default false;

    Class<?>[] configuration() default {};

    Class<?> fallback() default void.class;

    Class<?> fallbackFactory() default void.class;

    String path() default "";

    String group() default "";

    boolean primary() default true;
}
