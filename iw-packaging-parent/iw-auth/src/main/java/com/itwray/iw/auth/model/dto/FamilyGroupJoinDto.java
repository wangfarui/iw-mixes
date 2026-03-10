package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 家庭组 加入DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭组 加入DTO")
public class FamilyGroupJoinDto {

    @Schema(title = "邀请码")
    @NotBlank(message = "邀请码不能为空")
    @Length(min = 8, max = 8, message = "邀请码长度必须为8位")
    private String inviteCode;
}
