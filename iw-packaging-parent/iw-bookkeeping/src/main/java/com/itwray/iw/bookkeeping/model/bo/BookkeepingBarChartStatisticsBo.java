package com.itwray.iw.bookkeeping.model.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账柱状图-图表统计对象
 *
 * @author farui.wang
 * @since 2025/5/27
 */
@Data
public class BookkeepingBarChartStatisticsBo {

    /**
     * 记录日期
     */
    private String recordDate;

    /**
     * 统计金额
     */
    private BigDecimal amount;
}
