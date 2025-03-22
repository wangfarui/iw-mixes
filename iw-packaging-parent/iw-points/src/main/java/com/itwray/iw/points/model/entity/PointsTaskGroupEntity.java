package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务分组表
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_group")
public class PointsTaskGroupEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父分组id
     */
    private Integer parentId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 是否置顶任务 0-否 1-是
     */
    private Integer isTop;

    /**
     * 排序 0-默认排序
     */
    private Integer sort;
}
