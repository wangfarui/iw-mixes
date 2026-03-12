package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 家庭成员默认共享开关 更新DTO
 *
 * @author wray
 * @since 2026-03-11
 */
@Data
@Schema(name = "家庭成员默认共享开关 更新DTO")
public class FamilyMemberDefaultSharedUpdateDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(title = "默认共享开关(0关闭 1开启)")
    @NotNull(message = "默认共享开关不能为空")
    @Min(value = 0, message = "默认共享开关只能是0或1")
    @Max(value = 1, message = "默认共享开关只能是0或1")
    private Integer defaultShared;
}
