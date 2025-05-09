package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.points.model.enums.TaskPlanCycleEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 任务计划表
 *
 * @author wray
 * @since 2025-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_plan")
public class PointsTaskPlanEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务备注
     */
    private String taskRemark;

    /**
     * 计划周期(1:每日, 2:每周, 3:每月, 4:每年, 5:自定义)
     */
    private TaskPlanCycleEnum planCycle;

    /**
     * 计划日期(开始日期)
     * <p>根据计划周期定时自增</p>
     */
    private LocalDate planDate;

    /**
     * 计划天数
     * <p>仅在 planCycle = 5 时生效</p>
     */
    private Integer cycleDays;

    /**
     * 下一次计划生成日期
     * <p>nextPlanDate = planDate - remindDays</p>
     * <p>当计算日期小于等于当天时, 就顺延到下一个周期开始, 直至 nextPlan > now() </p>
     */
    private LocalDate nextPlanDate;

    /**
     * 提前提醒天数
     */
    private Integer remindDays;

    /**
     * 截止天数
     * <p>生成后的任务截止日期 = planDate + deadlineDays</p>
     */
    private Integer deadlineDays;

    /**
     * 奖励积分
     */
    private Integer rewardPoints;

    /**
     * 处罚积分
     */
    private Integer punishPoints;

    /**
     * 状态(0禁用 1启用)
     */
    private Integer status;
}
