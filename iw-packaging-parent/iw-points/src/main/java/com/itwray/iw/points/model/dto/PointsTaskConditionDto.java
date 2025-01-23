package com.itwray.iw.points.model.dto;

import com.itwray.iw.points.model.enums.PointsTaskPeriodicIntervalEnum;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicTypeEnum;
import lombok.Data;

/**
 * 任务查询条件
 *
 * @author wray
 * @since 2025/1/14
 */
@Data
public class PointsTaskConditionDto {

    /**
     * 周期任务类型
     */
    private PointsTaskPeriodicTypeEnum periodicType;

    /**
     * 周期间隔
     */
    private PointsTaskPeriodicIntervalEnum periodicInterval;
}
