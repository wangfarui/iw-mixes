package com.itwray.iw.bookkeeping.model.vo.yearly.income;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收入洞察
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsIncomeInsightsVo {

    /**
     * 收入最高的一天的金额
     */
    private BigDecimal maxDayAmount;

    /**
     * 收入最高的一天的日期，格式例如 2025-12-15
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate maxDayDate;

    /**
     * 收入最高月份的金额
     */
    private BigDecimal maxMonthAmount;

    /**
     * 收入最高月份的名称，例如 10月
     */
    private String maxMonthName;

    /**
     * 大额收入比例（>¥100 的笔数占比，0-100）
     */
    private BigDecimal largeIncomeRatio;

    /**
     * 月均收入
     */
    private BigDecimal avgMonthAmount;

}
