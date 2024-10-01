package com.itwray.iw.points.model.bo;

import lombok.Data;

/**
 * 积分记录 统计BO
 *
 * @author wray
 * @since 2024/9/30
 */
@Data
public class PointsRecordsStatisticsBo {

    /**
     * 积分变动类型(1表示增加, 2表示扣减)
     */
    private Integer transactionType;

    /**
     * 积分变动总数量(可以是正数或负数)
     */
    private Integer totalPoints;

}
