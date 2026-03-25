package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.auth.model.enums.FamilyMemberRoleEnum;
import com.itwray.iw.auth.model.enums.FamilyMemberStatusEnum;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.web.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 家庭成员表
 *
 * @author wray
 * @since 2024-03-10
 */
@TableName("auth_family_member")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthFamilyMemberEntity extends BaseEntity<Integer> {

    /**
     * 成员ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 家庭组ID
     */
    private Integer groupId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 角色 (1-群主, 2-家长, 3-成员, 4-儿童)
     */
    private FamilyMemberRoleEnum role;

    /**
     * 默认共享开关
     *
     * @see BoolEnum#getCode()
     */
    private Integer defaultShared;

    /**
     * 共享数据查看范围
     *
     * @see BoolEnum#getCode()
     */
    private Integer queryOnlyMyself;

    /**
     * 状态 (1-正常, 2-已退出, 3-已移除)
     */
    private FamilyMemberStatusEnum status;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
}
