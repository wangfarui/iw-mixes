package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务执行记录表
 *
 * @author wray
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_task_records")
public class PointsTaskRecordsEntity extends UserEntity<Integer> {

    /**
     * 执行记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 执行时间
     */
    private LocalDateTime recordTime;

    /**
     * 实际积分
     */
    private Integer actualPoints;
}
