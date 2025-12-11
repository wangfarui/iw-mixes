package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支出标签占比
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsConsumeTagsVo {

    private Integer dictId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 出现次数
     */
    private Integer count;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 次数占比百分比（0-100）
     */
    private BigDecimal ratio;

    /**
     * 金额占比百分比（0-100）
     */
    private BigDecimal amountRatio;
}
