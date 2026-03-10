package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.BaseEntity;
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
public class AuthUserEntity extends BaseEntity<Integer> {

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
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 用户角色类型
     */
    private Integer roleType;

    /**
     * 当前家庭组ID (0-个人模式)
     */
    private Integer familyGroupId;

    /**
     * 新用户
     */
    @TableField(exist = false)
    private boolean newUser;
}
