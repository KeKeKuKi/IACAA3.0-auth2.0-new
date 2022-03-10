package com.gapache.uid.utils;

/**
 * {@code ValuedEnum} defines an enumeration which is bounded to a value, you
 * may implements this interface when you defines such kind of enumeration, that
 * you can use {@link EnumUtils} to simplify parse and valueOf operation.
 *  
 * @author HuSen
 * create on 2020/1/9 15:15
 */
public interface ValuedEnum<T> {
    T value();
}
