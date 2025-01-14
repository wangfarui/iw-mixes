package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicIntervalEnum;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicTypeEnum;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 周期性任务表
 *
 * @author wray
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_periodic")
public class PointsTaskPeriodicEntity extends IdEntity<Integer> {

    /**
     * 任务id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * 周期任务类型
     */
    private PointsTaskPeriodicTypeEnum periodicType;

    /**
     * 周期间隔
     */
    private PointsTaskPeriodicIntervalEnum periodicInterval;

    /**
     * 未执行任务扣除的积分
     */
    private Integer penaltyPoints;

    /**
     * 周期内最大执行次数
     */
    private Integer maxExecutionCount;
}
