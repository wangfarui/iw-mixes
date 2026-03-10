package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.auth.model.enums.FamilyInviteStatusEnum;
import com.itwray.iw.web.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 家庭邀请表
 *
 * @author wray
 * @since 2024-03-10
 */
@TableName("auth_family_invite")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthFamilyInviteEntity extends BaseEntity<Integer> {

    /**
     * 邀请ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 家庭组ID
     */
    private Integer groupId;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请人用户ID
     */
    private Integer inviterUserId;

    /**
     * 有效时长(小时)
     */
    private Integer validHours;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 状态 (1-待使用, 2-已使用, 4-已过期)
     */
    private FamilyInviteStatusEnum status;
}
