package com.itwray.iw.bookkeeping.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账支出统计总数据 VO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingConsumeStatisticsTotalVo {

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 消费记录总数
     */
    private Integer totalRecordNum;
}
