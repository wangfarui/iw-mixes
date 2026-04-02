package com.itwray.iw.auth.model.dto;

import com.itwray.iw.auth.model.enums.AiTaskStatusEnum;
import com.itwray.iw.auth.model.enums.AiToolTypeEnum;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * AI任务新增DTO
 *
 * @author wray
 * @since 2026-03-26
 */
@Data
@Schema(name = "AI任务新增DTO")
public class AiTaskAddDto implements AddDto {

    @Schema(title = "任务标题")
    @NotBlank(message = "任务标题不能为空")
    @Length(max = 80, message = "任务标题不能超过80字符")
    private String title;

    @Schema(title = "会话任务描述")
    @Length(max = 255, message = "描述不能超过255字符")
    private String description;

    @Schema(title = "工具类型")
    @NotNull(message = "工具类型不能为空")
    private AiToolTypeEnum toolType;

    @Schema(title = "sessionKey")
    @NotBlank(message = "sessionKey不能为空")
    @Length(max = 128, message = "sessionKey不能超过128字符")
    private String sessionKey;

    @Schema(title = "任务状态")
    @NotNull(message = "任务状态不能为空")
    private AiTaskStatusEnum taskStatus;

    @Schema(title = "所属项目")
    @Length(max = 64, message = "所属项目不能超过64字符")
    private String projectName;

    @Schema(title = "工作区路径")
    @Length(max = 255, message = "工作区路径不能超过255字符")
    private String workspacePath;

    @Schema(title = "模型名称")
    @Length(max = 64, message = "模型名称不能超过64字符")
    private String modelName;

    @Schema(title = "git分支")
    @Length(max = 128, message = "git分支不能超过128字符")
    private String gitBranch;

    @Schema(title = "记录文件路径")
    @Length(max = 512, message = "记录文件路径不能超过512字符")
    private String transcriptPath;

    @Schema(title = "恢复命令")
    @Length(max = 255, message = "恢复命令不能超过255字符")
    private String resumeCommand;
}
