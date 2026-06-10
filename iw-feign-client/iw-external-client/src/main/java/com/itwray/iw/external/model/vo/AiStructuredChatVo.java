package com.itwray.iw.external.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AI结构化对话响应VO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "AI结构化对话响应VO")
public class AiStructuredChatVo {

    @Schema(title = "模型原始文本响应")
    private String content;
}
