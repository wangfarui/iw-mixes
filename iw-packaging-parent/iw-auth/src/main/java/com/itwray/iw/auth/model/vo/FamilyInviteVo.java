package com.itwray.iw.auth.model.vo;

import com.itwray.iw.auth.model.enums.FamilyInviteStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家庭邀请 VO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭邀请 VO")
public class FamilyInviteVo {

    @Schema(title = "邀请ID")
    private Integer id;

    @Schema(title = "家庭组ID")
    private Integer groupId;

    @Schema(title = "家庭组名称")
    private String groupName;

    @Schema(title = "邀请码")
    private String inviteCode;

    @Schema(title = "邀请人用户ID")
    private Integer inviterUserId;

    @Schema(title = "邀请人姓名")
    private String inviterName;

    @Schema(title = "有效时长(小时)")
    private Integer validHours;

    @Schema(title = "过期时间")
    private LocalDateTime expireTime;

    @Schema(title = "状态 (1-待使用, 2-已使用, 4-已过期)")
    private FamilyInviteStatusEnum status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
