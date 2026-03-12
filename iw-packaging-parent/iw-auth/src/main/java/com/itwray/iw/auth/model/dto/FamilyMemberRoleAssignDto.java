package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 家庭成员 角色分配DTO
 *
 * @author wray
 * @since 2026-03-11
 */
@Data
@Schema(name = "家庭成员 角色分配DTO")
public class FamilyMemberRoleAssignDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(title = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @Schema(title = "角色编码(2-家长,3-成员,4-儿童)")
    @NotNull(message = "角色不能为空")
    private Integer role;
}
