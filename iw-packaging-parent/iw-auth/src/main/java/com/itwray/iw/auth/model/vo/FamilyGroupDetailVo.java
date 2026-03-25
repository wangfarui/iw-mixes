package com.itwray.iw.auth.model.vo;

import com.itwray.iw.auth.model.enums.FamilyMemberRoleEnum;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 家庭组 详情VO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭组 详情VO")
public class FamilyGroupDetailVo implements DetailVo {

    @Schema(title = "家庭组ID")
    private Integer id;

    @Schema(title = "家庭组名称")
    private String groupName;

    @Schema(title = "家庭组头像")
    private String groupAvatar;

    @Schema(title = "家庭组描述")
    private String groupDesc;

    @Schema(title = "群主用户ID")
    private Integer ownerUserId;

    @Schema(title = "最大成员数")
    private Integer maxMember;

    @Schema(title = "状态 (1-启用, 0-禁用)")
    private Integer status;

    @Schema(title = "当前用户角色 (1-群主, 2-家长, 3-成员, 4-儿童)")
    private FamilyMemberRoleEnum currentUserRole;

    @Schema(title = "当前用户新建默认共享开关(0关闭 1开启)")
    private Integer defaultShared;

    @Schema(title = "当前用户共享数据查看范围(0家庭共享 1仅自己)")
    private Integer queryOnlyMyself;
}
