package com.itwray.iw.bookkeeping.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账记录 统计VO
 *
 * @author wray
 * @since 2024/9/23
 */
@Data
public class BookkeepingRecordsStatisticsVo {

    /**
     * 消费金额
     */
    private BigDecimal consume;

    /**
     * 收入金额
     */
    private BigDecimal income;
}
