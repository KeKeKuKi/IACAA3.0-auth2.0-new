package com.gapache.commons.utils;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2020/4/5 16:16
 */
public class IStringUtils {

    public static byte[] getBytes(String string) {
        return string != null ? string.getBytes(StandardCharsets.UTF_8) : new byte[0];
    }

    public static String newString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
