package com.itwray.iw.bookkeeping.model.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 记账记录 列表DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class BookkeepingRecordListDto {

    /**
     * 记账日期
     */
    private LocalDate recordDate;
}
