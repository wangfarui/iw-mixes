package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分记录表
 *
 * @author wray
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_records")
public class PointsRecordsEntity extends UserEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 积分变动类型(1表示增加, 2表示扣减)
     */
    private Integer transactionType;

    /**
     * 积分变动数量(可以是正数或负数)
     */
    private Integer points;

    /**
     * 积分来源
     */
    private String source;

    /**
     * 积分变动备注
     */
    private String remark;
}
