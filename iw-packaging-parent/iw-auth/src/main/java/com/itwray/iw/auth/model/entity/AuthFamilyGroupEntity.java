package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家庭组表
 *
 * @author wray
 * @since 2024-03-10
 */
@TableName("auth_family_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthFamilyGroupEntity extends UserEntity<Integer> {

    /**
     * 家庭组ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 家庭组名称
     */
    private String groupName;

    /**
     * 家庭组头像
     */
    private String groupAvatar;

    /**
     * 家庭组描述
     */
    private String groupDesc;

    /**
     * 群主用户ID
     */
    private Integer ownerUserId;

    /**
     * 最大成员数
     */
    private Integer maxMember;

    /**
     * 状态 (1-启用, 0-禁用)
     */
    private Integer status;
}
