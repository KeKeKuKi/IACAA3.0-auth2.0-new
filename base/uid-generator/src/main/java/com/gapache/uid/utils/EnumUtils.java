package com.gapache.uid.utils;

import org.springframework.util.Assert;

/**
 * EnumUtils provides the operations for {@link ValuedEnum} such as Parse, value of...
 *
 * @author HuSen
 * create on 2020/1/9 15:15
 */
public abstract class EnumUtils {

    /**
     * Parse the bounded value into ValuedEnum
     *
     * @param clz   class
     * @param value value
     * @return T extends ValuedEnum<V>
     */
    public static <T extends ValuedEnum<V>, V> T parse(Class<T> clz, V value) {
        Assert.notNull(clz, "clz can not be null");
        if (value == null) {
            return null;
        }

        for (T t : clz.getEnumConstants()) {
            if (value.equals(t.value())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Null-safe valueOf function
     *
     * @param <T>      T
     * @param enumType enumType
     * @param name     name
     * @return enum
     */
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        if (name == null) {
            return null;
        }
        return Enum.valueOf(enumType, name);
    }
}
