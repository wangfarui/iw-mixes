package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.points.model.enums.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新任务状态DTO
 *
 * @author wray
 * @since 2025/3/25
 */
@Data
@Schema(name = "更新任务状态DTO")
public class TaskBasicsUpdateStatusDto {

    @Schema(title = "任务id")
    @NotNull(message = "任务id不能为空")
    private Integer id;

    @Schema(title = "任务状态 0-未完成 1-已完成 2-已放弃 3-已删除")
    @NotNull(message = "任务状态不能为空")
    private TaskStatusEnum taskStatus;
}
