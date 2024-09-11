package com.itwray.iw.web.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带用户数据权限的实体
 *
 * @author wray
 * @since 2024/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserEntity extends BaseEntity {

    /**
     * 用户id
     */
    private Integer userId;
}
