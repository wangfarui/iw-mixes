package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分任务表
 *
 * @author wray
 * @since 2024-10-9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task")
public class PointsTaskEntity extends UserEntity {

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
     * 任务积分分数(可以是正数或负数)
     */
    private Integer taskPoints;
}
