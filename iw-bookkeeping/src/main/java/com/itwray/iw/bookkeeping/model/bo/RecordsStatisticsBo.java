package com.itwray.iw.bookkeeping.model.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账记录统计BO
 *
 * @author wray
 * @since 2024/9/23
 */
@Data
public class RecordsStatisticsBo {

    /**
     * 记录类型
     */
    private Integer recordCategory;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;
}
