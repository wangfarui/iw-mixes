package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支出分类占比
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsConsumeCategoriesVo {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 占比百分比（0-100）
     */
    private BigDecimal ratio;
}
