package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账记录 年度统计-支出VO
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsYearStatisticsConsumeVo {

    /**
     * 年度统计
     */
    private BookkeepingRecordsConsumeSummaryVo yearStatistics;

    /**
     * 月度趋势（12个数据，按1月-12月顺序）
     */
    private List<BigDecimal> monthlyData;

    /**
     * 支出分类占比
     */
    private List<BookkeepingRecordsConsumeCategoriesVo> consumeCategories;

    /**
     * 支出标签占比
     */
    private List<BookkeepingRecordsConsumeTagsVo> consumeTags;

    /**
     * 支出Top10
     */
    private List<BookkeepingRecordsConsumeTopVo> topConsumeList;

    /**
     * 支出洞察
     */
    private BookkeepingRecordsConsumeInsightsVo insights;
}
