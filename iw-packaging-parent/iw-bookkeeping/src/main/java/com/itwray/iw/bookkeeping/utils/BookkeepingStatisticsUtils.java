package com.itwray.iw.bookkeeping.utils;

import com.itwray.iw.bookkeeping.model.bo.BookkeepingBarChartStatisticsBo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 记账统计工具
 *
 * @author wray
 * @since 2025/12/11
 */
public class BookkeepingStatisticsUtils {

    /**
     * 转换年度统计-柱状图数据
     *
     * @param yearDate 年度日期
     * @param list     柱状图-年度数据
     * @return 年度内按月份正序顺序排序的数据
     */
    public static List<BigDecimal> convertToBarChartYearStatisticsBo(LocalDate yearDate, List<BookkeepingBarChartStatisticsBo> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, BigDecimal> recordDateMap = list.stream().collect(Collectors.toMap(
                BookkeepingBarChartStatisticsBo::getRecordDate, BookkeepingBarChartStatisticsBo::getAmount
        ));
        List<BigDecimal> result = new ArrayList<>();
        int year = yearDate.getYear();
        for (int i = 1; i <= 12; i++) {
            String recordDate = year + "-" + (i < 10 ? "0" + i : i);
            result.add(Optional.ofNullable(recordDateMap.get(recordDate)).orElse(BigDecimal.ZERO));
        }
        return result;
    }

    /**
     * 转换月度统计-柱状图数据
     *
     * @param monthDate 月度日期
     * @param list      柱状图-月度数据
     * @return 月度内按天数正序顺序排序的数据
     */
    public static List<BigDecimal> convertToBarChartMonthStatisticsBo(LocalDate monthDate, List<BookkeepingBarChartStatisticsBo> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, BigDecimal> recordDateMap = list.stream().collect(Collectors.toMap(
                BookkeepingBarChartStatisticsBo::getRecordDate, BookkeepingBarChartStatisticsBo::getAmount
        ));
        List<BigDecimal> result = new ArrayList<>();
        int year = monthDate.getYear();
        int month = monthDate.getMonthValue();
        String yearMonth = year + "-" + (month < 10 ? "0" + month : month) + "-";
        for (int i = 1; i <= monthDate.lengthOfMonth(); i++) {
            String recordDate = yearMonth + (i < 10 ? "0" + i : i);
            result.add(Optional.ofNullable(recordDateMap.get(recordDate)).orElse(BigDecimal.ZERO));
        }
        return result;
    }
}
