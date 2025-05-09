package com.itwray.iw.points.model.dto.plan;

import com.itwray.iw.points.model.enums.TaskPlanCycleEnum;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 任务计划表 新增DTO
 *
 * @author wray
 * @since 2025-05-07
 */
@Data
@Schema(name = "任务计划表 新增DTO")
public class PointsTaskPlanAddDto implements AddDto {

    @Schema(title = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @Schema(title = "任务备注")
    private String taskRemark;

    @Schema(title = "计划周期(1:每日, 2:每周, 3:每月, 4:每年, 5:自定义)")
    @NotNull(message = "计划周期不能为空")
    private TaskPlanCycleEnum planCycle;

    @Schema(title = "计划日期(开始日期)")
    @NotNull(message = "计划日期不能为空")
    private LocalDate planDate;

    @Schema(title = "计划天数")
    private Integer cycleDays;

    @Schema(title = "提前提醒天数")
    private Integer remindDays;

    @Schema(title = "截止天数")
    private Integer deadlineDays;

    @Schema(title = "奖励积分")
    private Integer rewardPoints;

    @Schema(title = "处罚积分")
    private Integer punishPoints;
}
