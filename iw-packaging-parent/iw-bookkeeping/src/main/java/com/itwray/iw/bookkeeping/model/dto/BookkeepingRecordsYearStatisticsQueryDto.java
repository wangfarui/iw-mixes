package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * 年度统计查询对象
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsYearStatisticsQueryDto {

    private Integer userId;

    /**
     * 统计年份
     */
    private String year;

    /**
     * 是否忽略不计入统计的账单
     */
    private Integer ignoreNotStatistics;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * 查询的记录类型
     */
    private Set<RecordCategoryEnum> recordCategories;
}
