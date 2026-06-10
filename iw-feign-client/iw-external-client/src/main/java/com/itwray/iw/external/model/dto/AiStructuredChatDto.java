package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * AI结构化对话请求DTO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "AI结构化对话请求DTO")
public class AiStructuredChatDto {

    @NotEmpty(message = "消息列表不能为空")
    @Schema(title = "对话消息列表")
    private List<AiChatMessageDto> messages;

    @Schema(title = "模型名称")
    private String model;

    @Schema(title = "最大token数")
    private Integer maxTokens;

    @Schema(title = "温度")
    private Double temperature;
}
