package com.itwray.iw.auth.model.dto;

import com.itwray.iw.auth.model.enums.AiTaskStatusEnum;
import com.itwray.iw.auth.model.enums.AiToolTypeEnum;
import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI任务分页DTO
 *
 * @author wray
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "AI任务分页DTO")
public class AiTaskPageDto extends PageDto {

    @Schema(title = "关键字(任务标题/摘要/sessionKey)")
    private String keyword;

    @Schema(title = "工具类型")
    private AiToolTypeEnum toolType;

    @Schema(title = "任务状态")
    private AiTaskStatusEnum taskStatus;

    @Schema(title = "所属项目")
    private String projectName;

    @Schema(title = "工作区关键字")
    private String workspaceKeyword;

    @Schema(title = "sessionKey")
    private String sessionKey;
}
