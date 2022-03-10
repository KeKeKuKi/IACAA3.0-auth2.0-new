package com.gapache.web;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * @author HuSen
 * @since 2020/11/20 9:31 上午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
public @interface Check {
}
