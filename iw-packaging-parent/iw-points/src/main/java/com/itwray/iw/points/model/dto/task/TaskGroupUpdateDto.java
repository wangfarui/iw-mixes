package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务分组表 更新DTO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@Schema(name = "任务分组表 更新DTO")
public class TaskGroupUpdateDto implements UpdateDto {

    @Schema(title = "id")
    private Integer id;
}
