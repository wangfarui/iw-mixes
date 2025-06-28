package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账支出 服务接口
 *
 * @author wray
 * @since 2024/10/15
 */
public interface BookkeepingConsumeService {

    /**
     * 统计月度总支出数据
     */
    BookkeepingStatisticsTotalVo totalStatistics(BookkeepingConsumeStatisticsDto dto);

    /**
     * 统计月度支出排行数据
     */
    List<BookkeepingStatisticsRankVo> rankStatistics(BookkeepingConsumeStatisticsDto dto);

    /**
     * 支出饼图图表统计
     */
    List<BookkeepingConsumeStatisticsCategoryVo> pieChartStatistics(BookkeepingConsumeCategoryStatisticsDto dto);

    /**
     * 支出柱状图图表统计
     */
    List<BigDecimal> barChartStatistics(BookkeepingConsumeStatisticsDto dto);
}
