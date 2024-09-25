package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordStartDate;

    /**
     * 记账记录结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordEndDate;
}
