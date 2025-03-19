package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 任务基础表 新增DTO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@Schema(name = "任务基础表 新增DTO")
public class TaskBasicsAddDto implements AddDto {

    @Schema(title = "父任务id")
    private Integer parentId;

    @Schema(title = "任务分组id 0-无分组(收集箱)")
    private Integer taskGroupId;

    @Schema(title = "任务名称")
    private String taskName;

    @Schema(title = "任务备注")
    private String taskRemark;

    @Schema(title = "截止日期(在重复任务中可被理解为开始日期)")
    private LocalDate deadlineDate;

    @Schema(title = "截止时间(在重复任务中可被理解为开始时间)")
    private LocalTime deadlineTime;

    @Schema(title = "优先级(数值越大,优先级越高) 0-无优先级")
    private Integer priority;

    @Schema(title = "是否置顶任务 0-否 1-是")
    private Integer isTop;

    @Schema(title = "排序 0-默认排序")
    private Integer sort;

}
