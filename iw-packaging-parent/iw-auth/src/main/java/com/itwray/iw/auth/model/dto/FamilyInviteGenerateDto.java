package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 家庭邀请 生成DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭邀请 生成DTO")
public class FamilyInviteGenerateDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(title = "有效时长(小时)", description = "1-168小时")
    @NotNull(message = "有效时长不能为空")
    @Min(value = 1, message = "有效时长不能小于1小时")
    @Max(value = 168, message = "有效时长不能超过168小时")
    private Integer validHours;
}
