package com.itwray.iw.bookkeeping.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 记账助手支出草稿确认响应 VO
 *
 * @author wray
 * @since 2026/4/16
 */
@Data
@Schema(name = "记账助手支出草稿确认响应 VO")
public class BookkeepingAssistantConfirmExpenseVo {

    @Schema(title = "解析日志ID")
    private Integer logId;

    @Schema(title = "记账记录ID")
    private Integer recordId;

    @Schema(title = "是否复用已确认结果")
    private Boolean reused;
}
