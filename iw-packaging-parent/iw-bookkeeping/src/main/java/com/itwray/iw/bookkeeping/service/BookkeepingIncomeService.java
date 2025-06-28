package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingIncomeStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账收入 服务接口
 *
 * @author farui.wang
 * @since 2025/5/27
 */
public interface BookkeepingIncomeService {

    BookkeepingStatisticsTotalVo totalStatistics(BookkeepingIncomeStatisticsDto dto);

    List<BookkeepingStatisticsRankVo> rankStatistics(BookkeepingIncomeStatisticsDto dto);

    List<BigDecimal> chartStatistics(BookkeepingIncomeStatisticsDto dto);
}
