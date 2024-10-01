package com.itwray.iw.points.model.vo;

import lombok.Data;

/**
 * 积分记录 统计VO
 *
 * @author wray
 * @since 2024/9/30
 */
@Data
public class PointsRecordsStatisticsVo {

    /**
     * 增加积分
     */
    private Integer increasePoints;

    /**
     * 扣减积分
     */
    private Integer deductPoints;
}
