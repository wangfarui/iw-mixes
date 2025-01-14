package com.itwray.iw.points.model.bo;

import com.itwray.iw.points.model.enums.PointsTaskPeriodicIntervalEnum;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicTypeEnum;
import com.itwray.iw.points.model.enums.PointsTaskTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实体完整对象
 *
 * @author wray
 * @since 2025/1/13
 */
@Data
public class PointsTaskFullBo {

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务类型
     */
    private PointsTaskTypeEnum taskType;

    /**
     * 任务基础积分
     */
    private Integer basePoints;

    /**
     * 是否允许自定义积分
     */
    private Boolean allowCustomPoints;

    /**
     * 截止时间
     */
    private LocalDateTime deadline;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
