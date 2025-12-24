package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 年度统计
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsConsumeSummaryVo {

    /**
     * 年度支出总额
     */
    private BigDecimal totalConsume;

    /**
     * 支出笔数
     */
    private Integer consumeCount;
}
