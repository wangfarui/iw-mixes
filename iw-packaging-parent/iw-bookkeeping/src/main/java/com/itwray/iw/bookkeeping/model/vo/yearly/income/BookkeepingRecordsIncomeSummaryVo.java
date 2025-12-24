package com.itwray.iw.bookkeeping.model.vo.yearly.income;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 年度统计
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsIncomeSummaryVo {

    /**
     * 年度收入总额
     */
    private BigDecimal totalIncome;

    /**
     * 收入笔数
     */
    private Integer incomeCount;
}
