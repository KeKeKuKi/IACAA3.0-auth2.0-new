package com.gapache.vertx.web.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author HuSen
 * @since 2021/3/3 9:28 上午
 */
public class ObjectUtils {

    public static boolean isSimpleValue(Class<?> arg0Class) {
        return arg0Class.equals(String.class)
                || arg0Class.equals(Byte.class)
                || arg0Class.equals(byte[].class)
                || arg0Class.equals(Character.class)
                || arg0Class.equals(Short.class)
                || arg0Class.equals(Integer.class)
                || arg0Class.equals(Long.class)
                || arg0Class.equals(Double.class)
                || arg0Class.equals(BigDecimal.class)
                || arg0Class.equals(BigInteger.class);
    }

}
