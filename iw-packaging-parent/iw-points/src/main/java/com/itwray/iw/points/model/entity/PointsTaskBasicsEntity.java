package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 任务基础表
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_basics")
public class PointsTaskBasicsEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父任务id
     */
    private Integer parentId;

    /**
     * 任务分组id 0-无分组(收集箱)
     */
    private Integer taskGroupId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务备注
     */
    private String taskRemark;

    /**
     * 任务状态 0-未完成 1-已完成 2-已放弃
     */
    private TaskStatusEnum taskStatus;

    /**
     * 截止日期(在重复任务中可被理解为开始日期)
     */
    private LocalDate deadlineDate;

    /**
     * 截止时间(在重复任务中可被理解为开始时间)
     */
    private LocalTime deadlineTime;

    /**
     * 优先级(数值越大,优先级越高) 0-无优先级
     */
    private Integer priority;

    /**
     * 是否置顶任务 0-否 1-是
     */
    private Integer isTop;

    /**
     * 排序 0-默认排序
     */
    private Integer sort;

    /**
     * 任务完成时间
     */
    private LocalDateTime doneTime;
}
