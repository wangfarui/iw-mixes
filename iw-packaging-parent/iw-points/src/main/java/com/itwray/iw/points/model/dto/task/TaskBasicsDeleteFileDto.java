package com.itwray.iw.points.model.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务删除附件dto
 *
 * @author wray
 * @since 2025/4/29
 */
@Data
public class TaskBasicsDeleteFileDto {

    @Schema(title = "任务id")
    private Integer taskId;

    @Schema(title = "文件路径")
    private String fileUrl;
}
