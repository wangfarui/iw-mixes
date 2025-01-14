package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 带用户数据权限的实体
 *
 * @author wray
 * @since 2024/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserEntity<ID extends Serializable> extends BaseEntity<ID> {

    /**
     * 用户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer userId;
}
