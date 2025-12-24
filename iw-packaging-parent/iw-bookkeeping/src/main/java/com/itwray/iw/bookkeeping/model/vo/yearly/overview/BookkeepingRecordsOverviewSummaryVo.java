package com.itwray.iw.bookkeeping.model.vo.yearly.overview;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 年度汇总统计数据
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsOverviewSummaryVo {

    /**
     * 年度支出总额（小数点2位）
     */
    private BigDecimal totalConsume;

    /**
     * 支出笔数
     */
    private Integer consumeCount;

    /**
     * 年度收入总额（小数点2位）
     */
    private BigDecimal totalIncome;

    /**
     * 收入笔数
     */
    private Integer incomeCount;

    /**
     * 年度净收入（收入-支出，可能为负）
     */
    private BigDecimal netIncome;

    public static BookkeepingRecordsOverviewSummaryVo empty() {
        BookkeepingRecordsOverviewSummaryVo summaryVo = new BookkeepingRecordsOverviewSummaryVo();
        summaryVo.setTotalConsume(BigDecimal.ZERO);
        summaryVo.setConsumeCount(0);
        summaryVo.setTotalIncome(BigDecimal.ZERO);
        summaryVo.setIncomeCount(0);
        summaryVo.setNetIncome(BigDecimal.ZERO);
        return summaryVo;
    }
}
