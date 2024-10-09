package com.itwray.iw.points.model.vo;

import lombok.Data;

/**
 * 积分任务 列表VO
 *
 * @author wray
 * @since 2024/10/9
 */
@Data
public class PointsTaskListVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务积分分数(可以是正数或负数)
     */
    private Integer taskPoints;
}
