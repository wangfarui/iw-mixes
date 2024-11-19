package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.time.LocalDate;

/**
 * 记账记录 统计DTO
 *
 * @author wray
 * @since 2024/9/23
 */
@Data
public class BookkeepingRecordsStatisticsDto {

    /**
     * 记账记录开始时间
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordStartDate;

    /**
     * 记账记录结束时间
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordEndDate;

    /**
     * 记录分类
     */
    private Integer recordType;
}
