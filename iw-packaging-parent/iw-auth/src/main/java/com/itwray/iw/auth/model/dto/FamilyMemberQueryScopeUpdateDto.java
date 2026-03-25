package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 家庭成员共享数据查看范围 更新DTO
 *
 * @author wray
 * @since 2026/3/23
 */
@Data
@Schema(name = "家庭成员共享数据查看范围 更新DTO")
public class FamilyMemberQueryScopeUpdateDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer groupId;

    @Schema(title = "共享数据查看范围(0家庭共享 1仅自己)")
    @NotNull(message = "共享数据查看范围不能为空")
    @Min(value = 0, message = "共享数据查看范围只能是0或1")
    @Max(value = 1, message = "共享数据查看范围只能是0或1")
    private Integer queryOnlyMyself;
}
