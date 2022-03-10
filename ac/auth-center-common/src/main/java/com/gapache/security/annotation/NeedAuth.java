package com.gapache.security.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/8/6 4:30 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NeedAuth {
    /**
     * 分类名称
     *
     * @return 分类名称
     */
    String value();
}
