package com.gapache.commons.model;


/**
 * @author HuSen
 * create on 2020/1/14 14:39
 */
public final class ThrowUtils {

    public static void throwIfTrue(boolean testBool, Error error) {
        if (testBool) {
            throw new BusinessException(error);
        }
    }

    public static void throwError(Error error) {
        throw new BusinessException(error);
    }
}
