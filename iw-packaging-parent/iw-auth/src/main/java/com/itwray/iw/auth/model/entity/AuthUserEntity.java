package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author wray
 * @since 2024/3/2
 */
@TableName("auth_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthUserEntity extends IdEntity<Integer> {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像（url地址）
     */
    private String avatar;

    /**
     * 账号是否过期
     */
    private Boolean accountNonExpired;

    /**
     * 账号是否锁定
     */
    private Boolean accountNonLocked;

    /**
     * 用户凭证是否过期
     */
    private Boolean credentialsNonExpired;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 新用户
     */
    @TableField(exist = false)
    private boolean newUser;
}
