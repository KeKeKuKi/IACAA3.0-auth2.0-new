package com.gapache.security.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/7/31 12:37 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSecurity {

}
