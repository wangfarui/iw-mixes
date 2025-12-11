package com.itwray.iw.bookkeeping.model.vo.yearly.overview;

import lombok.Data;

/**
 * 记账记录 年度统计-总览VO
 *
 * @author wray
 * @since 2024/9/23
 */
@Data
public class BookkeepingRecordsYearStatisticsOverviewVo {

    /**
     * 年度统计数据
     */
    private BookkeepingRecordsOverviewSummaryVo yearStatistics;

    /**
     * 月度趋势数据（12个月，按1月-12月顺序）
     */
    private BookkeepingRecordsOverviewMonthlyVo monthlyData;

    /**
     * 记账习惯数据
     */
    private BookkeepingRecordsOverviewHabitsVo recordingHabits;
}
