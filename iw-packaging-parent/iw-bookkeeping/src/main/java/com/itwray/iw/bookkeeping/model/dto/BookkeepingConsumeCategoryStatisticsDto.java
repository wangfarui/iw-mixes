package com.itwray.iw.bookkeeping.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 记账支出分类统计 DTO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingConsumeCategoryStatisticsDto extends BookkeepingConsumeStatisticsDto {

    /**
     * 是否查询上个月的数据
     */
    private Boolean isQueryLastMonth;

    /**
     * 记账分类
     */
    private List<Integer> recordTypeList;
}
