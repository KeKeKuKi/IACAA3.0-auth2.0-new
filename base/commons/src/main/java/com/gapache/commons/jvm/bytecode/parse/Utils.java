package com.gapache.commons.jvm.bytecode.parse;

import java.util.function.Consumer;

/**
 * @author HuSen
 * create on 2020/3/28 00:37
 */
public class Utils {

    public static String formatIndex(int index) {
        return "        ".substring(String.valueOf(index).length()) + "#" + index;
    }

    public static String formatValueOrIndex(String type, String value) {
        String maxDistance = "                    ";
        return maxDistance.substring(type.length()) + value;
    }

    public static String formatDescription(String pre, String description) {
        String maxDistance = "                    ";
        return maxDistance.substring(pre.length()) + description;
    }

    public static int readData(String content, int count, int point, Consumer<String> hexConsumer) {
        int newPoint = point + count;
        String hexString = content.substring(point, newPoint);
        hexConsumer.accept(hexString);
        return newPoint;
    }

    public static int hexToInt(String hex) {
        return Integer.valueOf(hex, 16);
    }

    public static long hexToLong(String hex) {
        return Long.valueOf(hex, 16);
    }
}
