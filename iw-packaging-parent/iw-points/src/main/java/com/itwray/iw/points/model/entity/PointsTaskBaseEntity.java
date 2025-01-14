package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.points.model.enums.PointsTaskTypeEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务基础表
 *
 * @author wray
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_base")
public class PointsTaskBaseEntity extends UserEntity<Integer> {

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
}
