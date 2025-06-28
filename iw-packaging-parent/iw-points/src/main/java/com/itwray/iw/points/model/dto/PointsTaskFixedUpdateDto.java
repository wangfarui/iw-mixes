package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 常用任务表 更新DTO
 *
 * @author wray
 * @since 2025-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "常用任务表 更新DTO")
public class PointsTaskFixedUpdateDto extends PointsTaskFixedAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
