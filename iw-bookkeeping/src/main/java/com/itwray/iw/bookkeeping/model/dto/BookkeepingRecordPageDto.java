package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.web.model.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 记账记录 分页DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingRecordPageDto extends PageDto {

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
