package com.gapache.vertx.web.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2021/3/2 2:20 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableZeusClients {

    Class<?>[] basePackageClasses() default {};
}
