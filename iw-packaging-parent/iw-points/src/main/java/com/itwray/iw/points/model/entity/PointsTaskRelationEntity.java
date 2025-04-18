package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务关联表
 *
 * @author wray
 * @since 2025-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_relation")
public class PointsTaskRelationEntity extends IdEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 奖励积分
     */
    private Integer rewardPoints;

    /**
     * 处罚积分
     */
    private Integer punishPoints;
}
