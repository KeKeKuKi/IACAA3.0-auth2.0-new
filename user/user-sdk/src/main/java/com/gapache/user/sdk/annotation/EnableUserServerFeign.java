package com.gapache.user.sdk.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/8/26 9:36 上午
 */
@Target(ElementType.TYPE)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableUserServerFeign {
}
