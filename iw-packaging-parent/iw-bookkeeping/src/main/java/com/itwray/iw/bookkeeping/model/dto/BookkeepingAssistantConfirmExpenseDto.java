package com.itwray.iw.bookkeeping.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账助手支出草稿确认 DTO
 *
 * @author wray
 * @since 2026/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "记账助手支出草稿确认 DTO")
public class BookkeepingAssistantConfirmExpenseDto extends BookkeepingRecordAddDto {

    @NotNull(message = "解析日志ID不能为空")
    @Schema(title = "解析日志ID")
    private Integer logId;
}
