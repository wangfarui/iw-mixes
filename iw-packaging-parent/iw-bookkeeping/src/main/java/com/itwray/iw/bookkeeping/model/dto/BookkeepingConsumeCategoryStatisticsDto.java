package com.itwray.iw.bookkeeping.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账支出分类统计 DTO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingConsumeCategoryStatisticsDto extends BookkeepingConsumeMonthStatisticsDto {

    /**
     * 是否查询上个月的数据
     */
    private Boolean isQueryLastMonth;
}
