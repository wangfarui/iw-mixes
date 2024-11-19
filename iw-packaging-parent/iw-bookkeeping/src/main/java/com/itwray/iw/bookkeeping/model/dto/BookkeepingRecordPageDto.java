package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.dto.PageDto;
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
