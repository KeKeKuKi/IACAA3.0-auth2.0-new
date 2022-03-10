package com.gapache.uid.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * create on 2020/1/10 15:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableUidGenerator {
}
