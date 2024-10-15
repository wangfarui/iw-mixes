package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeMonthStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsTotalVo;

import java.util.List;

/**
 * 记账支出 接口服务
 *
 * @author wray
 * @since 2024/10/15
 */
public interface BookkeepingConsumeService {

    /**
     * 统计月度总支出数据
     */
    BookkeepingConsumeStatisticsTotalVo totalStatistics(BookkeepingConsumeMonthStatisticsDto dto);

    /**
     * 统计月度支出排行数据
     */
    List<BookkeepingConsumeStatisticsRankVo> rankStatistics(BookkeepingConsumeMonthStatisticsDto dto);

    /**
     * 统计月度支出分类数据
     */
    List<BookkeepingConsumeStatisticsCategoryVo> categoryStatistics(BookkeepingConsumeCategoryStatisticsDto dto);
}
