package com.gapache.uid.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtils provides date formatting, parsing
 *
 * @author HuSen
 * create on 2020/1/9 15:15
 */
public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * Patterns
     */
    public static final String DAY_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final Date DEFAULT_DATE = DateUtils.parseByDayPattern("1970-01-01");

    /**
     * Parse date by 'yyyy-MM-dd' pattern
     *
     * @param str dateStr
     * @return Date
     */
    public static Date parseByDayPattern(String str) {
        return parseDate(str, DAY_PATTERN);
    }

    /**
     * Parse date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @param str dateStr
     * @return Date
     */
    public static Date parseByDateTimePattern(String str) {
        return parseDate(str, DATETIME_PATTERN);
    }

    /**
     * Parse date without Checked exception
     *
     * @param str     dateStr
     * @param pattern pattern
     * @return Date
     * @throws RuntimeException when ParseException occurred
     */
    public static Date parseDate(String str, String pattern) {
        try {
            return parseDate(str, new String[]{pattern});
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Format date into string
     *
     * @param date    Date
     * @param pattern pattern
     * @return dateStr
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * Format date by 'yyyy-MM-dd' pattern
     *
     * @param date Date
     * @return dateStr
     */
    public static String formatByDayPattern(Date date) {
        if (date != null) {
            return DateFormatUtils.format(date, DAY_PATTERN);
        } else {
            return null;
        }
    }

    /**
     * Format date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @param date Date
     * @return dateStr
     */
    public static String formatByDateTimePattern(Date date) {
        return DateFormatUtils.format(date, DATETIME_PATTERN);
    }

    /**
     * Get current day using format date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @return CurrentDayByDayPattern
     */
    public static String getCurrentDayByDayPattern() {
        Calendar cal = Calendar.getInstance();
        return formatByDayPattern(cal.getTime());
    }

}
