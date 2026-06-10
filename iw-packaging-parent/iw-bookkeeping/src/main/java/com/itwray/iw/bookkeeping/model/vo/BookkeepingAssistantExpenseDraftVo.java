package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 记账助手支出草稿 VO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "记账助手支出草稿 VO")
public class BookkeepingAssistantExpenseDraftVo {

    @Schema(title = "记录日期")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordDate;

    @Schema(title = "记录类型(固定为1支出)")
    private Integer recordCategory;

    @Schema(title = "记录来源")
    private String recordSource;

    @Schema(title = "金额")
    private BigDecimal amount;

    @Schema(title = "记录分类")
    private Integer recordType;

    @Schema(title = "记录标签")
    private List<Integer> recordTags;

    @Schema(title = "记录图标")
    private String recordIcon;

    @Schema(title = "是否计入统计")
    private Integer isStatistics;

    @Schema(title = "是否共享给家庭")
    private Integer shared;
}
