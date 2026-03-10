package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 转让群主 DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(description = "转让群主DTO")
public class FamilyGroupTransferDto {

    @Schema(description = "家庭组ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(description = "新群主用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "新群主用户ID不能为空")
    private Integer newOwnerUserId;
}
