package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.itwray.iw.common.constants.BoolEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体对象
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseEntity extends IdEntity {

    /**
     * 是否删除，默认false
     * <p>false -> 未删除</p>
     * <p>true -> 已删除</p>
     *
     * @see BoolEnums
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
