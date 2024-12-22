package com.itwray.iw.points.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分合计表
 *
 * @author wray
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_total")
public class PointsTotalEntity extends UserEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 积分余额
     */
    private Integer pointsBalance;

    /**
     * 是否删除，默认false
     * <p>false -> 未删除</p>
     * <p>true -> 已删除</p>
     *
     * @see BoolEnum
     */
    @TableField(exist = false)
    private Boolean deleted;
}
