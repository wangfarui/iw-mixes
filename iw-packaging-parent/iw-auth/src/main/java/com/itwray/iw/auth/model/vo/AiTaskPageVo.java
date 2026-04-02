package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.auth.model.enums.AiTaskStatusEnum;
import com.itwray.iw.auth.model.enums.AiToolTypeEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.PageRecordVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI任务分页VO
 *
 * @author wray
 * @since 2026-03-26
 */
@Data
@Schema(name = "AI任务分页VO")
public class AiTaskPageVo implements PageRecordVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "任务标题")
    private String title;

    @Schema(title = "会话任务描述")
    private String description;

    @Schema(title = "工具类型")
    private AiToolTypeEnum toolType;

    @Schema(title = "sessionKey")
    private String sessionKey;

    @Schema(title = "任务状态")
    private AiTaskStatusEnum taskStatus;

    @Schema(title = "所属项目")
    private String projectName;

    @Schema(title = "工作区路径")
    private String workspacePath;

    @Schema(title = "模型名称")
    private String modelName;

    @Schema(title = "git分支")
    private String gitBranch;

    @Schema(title = "记录文件路径")
    private String transcriptPath;

    @Schema(title = "恢复命令")
    private String resumeCommand;

    @Schema(title = "最近活跃时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime lastActiveAt;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime updateTime;
}
