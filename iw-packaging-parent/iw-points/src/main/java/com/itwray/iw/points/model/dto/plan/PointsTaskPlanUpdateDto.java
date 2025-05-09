package com.itwray.iw.points.model.dto.plan;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务计划表 更新DTO
 *
 * @author wray
 * @since 2025-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "任务计划表 更新DTO")
public class PointsTaskPlanUpdateDto extends PointsTaskPlanAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
