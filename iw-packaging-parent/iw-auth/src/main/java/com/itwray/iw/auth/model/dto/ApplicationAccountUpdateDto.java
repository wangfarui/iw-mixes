package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账号信息 更新DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "应用账号信息 更新DTO")
public class ApplicationAccountUpdateDto extends ApplicationAccountAddDto implements UpdateDto {

    @Schema(title = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @Schema(title = "是否更新密码")
    private Boolean updatePassword;
}
