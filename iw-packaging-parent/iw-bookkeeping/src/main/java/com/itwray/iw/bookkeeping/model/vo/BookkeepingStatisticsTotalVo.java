package com.itwray.iw.bookkeeping.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账(支出/收入)统计总数据 VO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingStatisticsTotalVo {

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 记账记录总数
     */
    private Integer totalRecordNum;
}
