package com.gapache.commons.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/3 3:24 下午
 */
public class TimeUtils {

    public enum Format {
        //
        _1("yyyy-MM-dd"),
        _2("yyyy-MM-dd HH:mm:ss"),
        _3("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        private final String value;

        Format(String value) {
            this.value = value;
        }
    }
    private static final Map<Format, DateTimeFormatter> FORMATTER_MAP;

    static {
        FORMATTER_MAP = new HashMap<>();
        for (Format format : Format.values()) {
            FORMATTER_MAP.put(format, DateTimeFormatter.ofPattern(format.value));
        }
    }

    public static String format(Format pattern, TemporalAccessor temporalAccessor) {
        return FORMATTER_MAP.get(pattern).format(temporalAccessor);
    }

    public static LocalDateTime parse(Format pattern, String text) {
        return LocalDateTime.parse(text.trim(), FORMATTER_MAP.get(pattern));
    }

    public static LocalDate parseLocalDate(Format pattern, String text) {
        return LocalDate.parse(text, FORMATTER_MAP.get(pattern));
    }
}
