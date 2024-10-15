package com.itwray.iw.bookkeeping.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账支出统计分类数据 VO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingConsumeStatisticsCategoryVo {

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 金额占比
     */
    private BigDecimal ratio;

    /**
     * 记录数量
     */
    private Integer recordNum;

    /**
     * 当前月总金额
     */
    private BigDecimal amount;

    /**
     * 当前月是否大于等于上个月的金额
     */
    private Boolean isGreaterThan;

    /**
     * 上个月相差金额（固定为正数）
     */
    private BigDecimal lastAmount;

}
