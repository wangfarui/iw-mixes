package com.itwray.iw.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

/**
 * 日期工具类
 *
 * @author wray
 * @since 2024/9/30
 */
public abstract class DateUtils {

    /**
     * 返回当前月的开始日期
     */
    public static LocalDate startDateOfNowMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * 返回当前月的结束日期
     */
    public static LocalDate endDateOfNowMonth() {
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 使用 YearMonth 获取当前年的当前月
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());

        // 获取当前月的最后一天
        return yearMonth.atEndOfMonth();
    }

    /**
     * 返回当前月的开始时间
     */
    public static LocalDateTime startTimeOfNowMonth() {
        // 获取当前日期的 LocalDate
        LocalDate currentDate = LocalDate.now();

        // 获取当前月的第一天
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

        // 将时间设置为 00:00:00
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIDNIGHT);
    }

    /**
     * 返回当前月的结束时间
     */
    public static LocalDateTime endTimeOfNowMonth() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 使用 YearMonth 获取当前月最后一天
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // 将时间设置为 23:59:59
        return LocalDateTime.of(lastDayOfMonth, LocalTime.of(23, 59, 59));
    }
}
