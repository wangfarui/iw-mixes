package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发送验证码 DTO
 *
 * @author wray
 * @since 2024/12/19
 */
@Data
public class SmsSendVerificationCodeDto {

    @NotBlank
    @Schema(title = "电话号码")
    private String phoneNumber;

    @NotBlank
    @Schema(title = "签名名称")
    private String signName;

    @NotBlank
    @Schema(title = "模板CODE")
    private String templateCode;

    @NotBlank
    @Schema(title = "模板参数")
    private String templateParam;
}
