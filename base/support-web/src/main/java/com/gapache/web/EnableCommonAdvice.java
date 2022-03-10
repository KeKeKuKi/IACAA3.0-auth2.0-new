package com.gapache.web;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2021/1/26 10:03 上午
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCommonAdvice {

}
