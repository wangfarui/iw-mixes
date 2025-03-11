package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 应用账号信息 - 刷新密码DTO
 *
 * @author wray
 * @since 2025/3/11
 */
@Data
@Schema(name = "应用账号信息 - 刷新密码DTO")
public class ApplicationAccountRefreshPasswordDto {

    @Schema(title = "验证码")
    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码固定为6位数字")
    private String verificationCode;

    @Schema(title = "需要刷新密码的应用账号id集合")
    private List<Integer> idList;

    @Schema(title = "历史AES,如果为空,则表示未加密")
    private String oldAes;

    @Schema(title = "是否刷新所有应用账号")
    private boolean refreshAll;

}
