package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.dto.AddDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 记账记录 新增DTO
 *
 * @author wray
 * @since 2024/8/29
 */
@Data
public class BookkeepingRecordAddDto implements AddDto {

    /**
     * 记录日期
     * <p>为空时，表示当前日期</p>
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordDate;

    /**
     * 记录类型
     */
    @NotNull(message = "记录类型不能为空")
    private RecordCategoryEnum recordCategory;

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

    /**
     * 记录标签
     */
    private List<Integer> recordTags;

    /**
     * 是否为激励记录
     */
    private Integer isExcitationRecord;
}
