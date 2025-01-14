package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 一次性任务表
 *
 * @author wray
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_once")
public class PointsTaskOnceEntity extends IdEntity<Integer> {

    /**
     * 任务id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * 截止时间
     */
    private LocalDateTime deadline;
}
