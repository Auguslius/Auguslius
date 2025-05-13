package org.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    /**
     * 将 Date 转换为 String
     *
     * @param date Date对象
     * @return 格式化后的字符串 (yyyy-MM-dd)
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        return sdf.format(date);
    }

    /**
     * 将 String 转换为 Date
     *
     * @param dateString 字符串格式的日期 (yyyy-MM-dd)
     * @return Date对象
     */
    public static Date stringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式错误，应为 yyyy-MM-dd", e);
        }
    }

    /**
     * 获取当前年月，格式为yyyy-MM
     */
    public static String getCurrentYearMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
