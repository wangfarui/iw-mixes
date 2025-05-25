package com.itwray.iw.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 *
 * @author wray
 * @since 2024/9/30
 */
public abstract class DateUtils {

    /**
     * 日期字符串格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间字符串格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间字符串格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * 日期时间的格式化器
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    /**
     * 日期的格式化器
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    /**
     * 时间的格式化器
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    /**
     * 返回当前月的开始日期
     */
    public static LocalDate startDateOfNowMonth() {
        return startDateOfMonth(LocalDate.now());
    }

    /**
     * 返回当前年的开始日期
     */
    public static LocalDate startDateOfNowYear() {
        return startDateOfYear(LocalDate.now());
    }

    /**
     * 返回指定月的开始日期
     */
    public static LocalDate startDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    /**
     * 返回指定年的开始日期
     */
    public static LocalDate startDateOfYear(LocalDate date) {
        return date.withDayOfYear(1);
    }

    /**
     * 返回当前月的结束日期
     */
    public static LocalDate endDateOfNowMonth() {
        return endDateOfMonth(LocalDate.now());
    }

    /**
     * 返回当前年的结束日期
     */
    public static LocalDate endDateOfNowYear() {
        return endDateOfYear(LocalDate.now());
    }

    /**
     * 返回指定月的结束日期
     */
    public static LocalDate endDateOfMonth(LocalDate date) {
        // 使用 YearMonth 获取当前年的当前月
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());

        // 获取当前月的最后一天
        return yearMonth.atEndOfMonth();
    }

    /**
     * 返回指定年的结束日期
     */
    public static LocalDate endDateOfYear(LocalDate date) {
        // 使用 YearMonth 获取当前年的当前月
        YearMonth yearMonth = YearMonth.of(date.getYear(), 12);

        // 获取当前月的最后一天
        return yearMonth.atEndOfMonth();
    }

    /**
     * 返回当前月的开始时间
     */
    public static LocalDateTime startTimeOfNowMonth() {
        return startTimeOfMonth(LocalDate.now());
    }

    /**
     * 返回指定月的开始时间
     */
    public static LocalDateTime startTimeOfMonth(LocalDate date) {
        // 获取当前月的第一天
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);

        // 将时间设置为 00:00:00
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIDNIGHT);
    }

    /**
     * 返回当前月的结束时间
     */
    public static LocalDateTime endTimeOfNowMonth() {
        return endTimeOfMonth(LocalDate.now());
    }

    /**
     * 返回指定月的结束时间
     */
    public static LocalDateTime endTimeOfMonth(LocalDate date) {
        // 使用 YearMonth 获取当前月最后一天
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // 将时间设置为 23:59:59
        return LocalDateTime.of(lastDayOfMonth, LocalTime.of(23, 59, 59));
    }

    /**
     * 格式化日期时间
     *
     * @param dateTime LocalDateTime
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMATTER);
    }
}
