package com.itwray.iw.points.model.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 查询任务分页VO
 *
 * @author wray
 * @since 2026/2/25
 */
@Data
@Schema(name = "查询任务列表VO")
public class TaskBasicsPageVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "任务分组名称")
    private String taskGroupName;

    @Schema(title = "任务名称")
    private String taskName;

    @Schema(title = "任务备注")
    private String taskRemark;

    @Schema(title = "任务状态 0-未完成 1-已完成 2-已放弃")
    private Integer taskStatus;

    @Schema(title = "截止日期(在重复任务中可被理解为开始日期)")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate deadlineDate;

    @Schema(title = "优先级(数值越大,优先级越高) 0-无优先级")
    private Integer priority;

    @Schema(title = "是否置顶任务 0-否 1-是")
    private Integer isTop;

    @Schema(title = "排序 0-默认排序")
    private Integer sort;

    @Schema(title = "任务完成时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime doneTime;

    @Schema(title = "创建日期")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime createTime;

}
