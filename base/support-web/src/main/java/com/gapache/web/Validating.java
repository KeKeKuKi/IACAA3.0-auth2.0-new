package com.gapache.web;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/11/19 9:39 上午
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validating {

}
