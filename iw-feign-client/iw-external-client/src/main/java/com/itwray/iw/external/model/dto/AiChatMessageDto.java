package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AI对话消息DTO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "AI对话消息DTO")
public class AiChatMessageDto {

    @Schema(title = "消息角色")
    private String role;

    @Schema(title = "消息内容")
    private String content;

    @Schema(title = "参与者名称")
    private String name;
}
