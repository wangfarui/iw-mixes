package com.itwray.iw.bookkeeping.model.vo.yearly.income;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账记录 年度统计-收入VO
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsYearStatisticsIncomeVo {

    /**
     * 年度统计
     */
    private BookkeepingRecordsIncomeSummaryVo yearStatistics;

    /**
     * 月度趋势（12个数据，按1月-12月顺序）
     */
    private List<BigDecimal> monthlyData;

    /**
     * 收入分类占比
     */
    private List<BookkeepingRecordsIncomeCategoriesVo> incomeCategories;

    /**
     * 收入Top10
     */
    private List<BookkeepingRecordsIncomeTopVo> topIncomeList;

    /**
     * 收入洞察
     */
    private BookkeepingRecordsIncomeInsightsVo insights;
}
