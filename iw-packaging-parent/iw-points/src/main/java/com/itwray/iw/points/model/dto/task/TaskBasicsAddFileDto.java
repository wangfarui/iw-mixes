package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.web.model.dto.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务添加附件dto
 *
 * @author wray
 * @since 2025/4/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskBasicsAddFileDto extends FileDto {

    @Schema(title = "任务id")
    private Integer taskId;
}
