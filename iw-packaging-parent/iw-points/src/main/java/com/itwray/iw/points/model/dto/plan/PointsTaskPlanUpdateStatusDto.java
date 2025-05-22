
package com.itwray.iw.points.model.dto.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新任务计划状态DTO
 *
 * @author wray
 * @since 2025/3/25
 */
@Data
@Schema(name = "更新任务计划状态DTO")
public class PointsTaskPlanUpdateStatusDto {

    @Schema(title = "任务计划id")
    @NotNull(message = "任务计划id不能为空")
    private Integer id;

    @Schema(title = "状态(0禁用 1启用)")
    @NotNull(message = "任务状态不能为空")
    private Integer status;
}
