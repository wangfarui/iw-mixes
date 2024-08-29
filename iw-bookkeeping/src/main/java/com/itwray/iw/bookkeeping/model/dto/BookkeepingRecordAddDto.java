package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 记账记录 新增DTO
 *
 * @author wray
 * @since 2024/8/29
 */
@Data
public class BookkeepingRecordAddDto {

    /**
     * 记录日期
     * <p>为空时，表示当前日期</p>
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;

    /**
     * 记录类型
     */
    @NotNull(message = "记录类型不能为空")
    private Integer recordCategory;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 金额
     */
    @NotNull(message = "记账金额不能为空")
    private BigDecimal amount;

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 备注
     */
    private String remark;
}
