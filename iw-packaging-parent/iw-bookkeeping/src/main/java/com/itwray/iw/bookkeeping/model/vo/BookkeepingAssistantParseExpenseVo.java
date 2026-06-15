package com.itwray.iw.bookkeeping.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账助手支出解析响应 VO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "记账助手支出解析响应 VO")
public class BookkeepingAssistantParseExpenseVo {

    @Schema(title = "日志ID")
    private Integer logId;

    @Schema(title = "解析状态")
    private String status;

    @Schema(title = "识别文本")
    private String recognizedText;

    @Schema(title = "解析置信度")
    private BigDecimal confidence;

    @Schema(title = "匹配的记账行为ID")
    private Integer matchedActionId;

    @Schema(title = "提示文案")
    private String message;

    @Schema(title = "是否支持自动记账")
    private Boolean autoSaveEligible;

    @Schema(title = "是否已自动保存")
    private Boolean autoSaved;

    @Schema(title = "自动保存生成的记账记录ID")
    private Integer recordId;

    @Schema(title = "是否复用已确认结果")
    private Boolean confirmReused;

    @Schema(title = "缺失字段")
    private List<String> missingFields;

    @Schema(title = "歧义提示")
    private List<String> ambiguities;

    @Schema(title = "支出草稿")
    private BookkeepingAssistantExpenseDraftVo draft;
}
