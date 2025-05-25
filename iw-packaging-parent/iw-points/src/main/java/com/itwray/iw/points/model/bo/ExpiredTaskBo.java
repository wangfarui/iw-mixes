package com.itwray.iw.points.model.bo;

import lombok.Data;

/**
 * 过期任务对象
 *
 * @author wray
 * @since 2025/4/21
 */
@Data
public class ExpiredTaskBo {

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 任务关联id
     */
    private Integer relationId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 惩罚积分
     */
    private Integer punishPoints;

    /**
     * 用户id
     */
    private Integer userId;
}
