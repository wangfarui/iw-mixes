package com.itwray.iw.points.model.vo.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.points.model.enums.TaskPlanCycleEnum;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务计划表 详情VO
 *
 * @author wray
 * @since 2025-05-07
 */
@Data
@Schema(name = "任务计划表 详情VO")
public class PointsTaskPlanDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "任务名称")
    private String taskName;

    @Schema(title = "任务备注")
    private String taskRemark;

    @Schema(title = "计划周期(1:每日, 2:每周, 3:每月, 4:每年, 5:自定义)")
    private TaskPlanCycleEnum planCycle;

    @Schema(title = "计划日期(开始日期)")
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

    @Schema(title = "状态(0禁用 1启用)")
    private Integer status;

    @Schema(title = "是否删除(true表示已删除, 默认false表示未删除)")
    private Integer deleted;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;
}
