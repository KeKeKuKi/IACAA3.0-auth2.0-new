package com.gapache.vertx.web.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2021/3/1 3:46 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestRouting {

    String value() default "";

    RequestMethod[] method() default {};
}
