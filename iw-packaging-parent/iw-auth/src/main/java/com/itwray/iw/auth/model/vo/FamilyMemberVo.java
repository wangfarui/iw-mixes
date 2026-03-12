package com.itwray.iw.auth.model.vo;

import com.itwray.iw.auth.model.enums.FamilyMemberRoleEnum;
import com.itwray.iw.auth.model.enums.FamilyMemberStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家庭成员 VO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭成员 VO")
public class FamilyMemberVo {

    @Schema(title = "成员ID")
    private Integer id;

    @Schema(title = "家庭组ID")
    private Integer groupId;

    @Schema(title = "用户ID")
    private Integer userId;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "头像")
    private String avatar;

    @Schema(title = "角色 (1-群主, 2-家长, 3-成员, 4-儿童)")
    private FamilyMemberRoleEnum role;

    @Schema(title = "状态 (1-正常, 2-已退出, 3-已移除)")
    private FamilyMemberStatusEnum status;

    @Schema(title = "加入时间")
    private LocalDateTime joinTime;
}
