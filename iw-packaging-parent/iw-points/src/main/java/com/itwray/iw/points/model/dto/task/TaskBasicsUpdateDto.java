package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务基础表 更新DTO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "任务基础表 更新DTO")
public class TaskBasicsUpdateDto extends TaskBasicsAddDto implements UpdateDto {

    @Schema(title = "id")
    private Integer id;
}
