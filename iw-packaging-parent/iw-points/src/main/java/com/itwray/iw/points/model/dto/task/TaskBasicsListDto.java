package com.itwray.iw.points.model.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 查询任务列表DTO
 *
 * @author wray
 * @since 2025/3/20
 */
@Data
@Schema(name = "查询任务列表DTO")
public class TaskBasicsListDto {


    @Schema(title = "父任务id")
    private Integer parentId;

    @Schema(title = "任务分组id 0-无分组(收集箱)")
    @NotNull(message = "任务分组不能为空")
    private Integer taskGroupId;

}
