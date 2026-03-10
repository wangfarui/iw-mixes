package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 家庭成员 移除DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭成员 移除DTO")
public class FamilyMemberRemoveDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(title = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Integer userId;
}
