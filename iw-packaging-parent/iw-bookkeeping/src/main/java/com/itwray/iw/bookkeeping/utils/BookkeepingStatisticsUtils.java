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

    public static List<BigDecimal> convertToBarChartStatisticsBo(LocalDate yearDate, List<BookkeepingBarChartStatisticsBo> list) {
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
}
