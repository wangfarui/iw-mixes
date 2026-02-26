package com.itwray.iw.points.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 查询任务分页DTO
 *
 * @author wray
 * @since 2026/2/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "查询任务分页DTO")
public class TaskBasicsPageDto extends PageDto {

    @Schema(title = "父任务id")
    private Integer parentId;

    @Schema(title = "任务状态 0-未完成 1-已完成 2-已放弃 3-已删除")
    private TaskStatusEnum taskStatus;

    @Schema(title = "任务名称")
    private String taskName;

    @Schema(title = "截止日期-开始")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate startDeadlineDate;

    @Schema(title = "截止日期-结束")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate endDeadlineDate;

    @Schema(title = "完成时间-开始")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate startDoneTime;

    @Schema(title = "完成时间-结束")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate endDoneTime;
}
