package com.gapache.jpa;

/**
 * @author HuSen
 * @since 2021/1/18 10:22 上午
 */
public final class FindUtils {

    private static final String PERCENT_SIGN = "%";

    public static String leftMatch(String origin) {
        return PERCENT_SIGN + origin;
    }

    public static String rightMatch(String origin) {
        return origin + PERCENT_SIGN;
    }

    public static String allMatch(String origin) {
        return PERCENT_SIGN + origin + PERCENT_SIGN;
    }
}
