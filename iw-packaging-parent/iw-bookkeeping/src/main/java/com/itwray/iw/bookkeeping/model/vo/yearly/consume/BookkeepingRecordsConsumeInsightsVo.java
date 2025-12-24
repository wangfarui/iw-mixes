package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 支出洞察
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsConsumeInsightsVo {

    /**
     * 支出最高的一天的金额
     */
    private BigDecimal maxDayAmount;

    /**
     * 支出最高的一天的日期，格式例如 2025-12-15
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate maxDayDate;

    /**
     * 支出最高月份的金额
     */
    private BigDecimal maxMonthAmount;

    /**
     * 支出最高月份的名称，例如 10月
     */
    private String maxMonthName;

    /**
     * 最常用支出标签
     */
    private String topTag;

    /**
     * 最常用标签的出现次数
     */
    private Integer topTagCount;

    /**
     * 最常用标签的出现金额
     */
    private BigDecimal topTagAmount;

    /**
     * 最少用支出标签
     */
    private String bottomTag;

    /**
     * 最少用标签的出现次数
     */
    private Integer bottomTagCount;

    /**
     * 最少用标签的出现金额
     */
    private BigDecimal bottomTagAmount;

    /**
     * 大额支出比例（>¥100 的笔数占比，0-100）
     */
    private BigDecimal largeExpenseRatio;

    /**
     * 月均支出
     */
    private BigDecimal avgMonthAmount;

}
