package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务关联表 更新DTO
 *
 * @author wray
 * @since 2025-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "任务关联表 更新DTO")
public class PointsTaskRelationUpdateDto extends PointsTaskRelationAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
