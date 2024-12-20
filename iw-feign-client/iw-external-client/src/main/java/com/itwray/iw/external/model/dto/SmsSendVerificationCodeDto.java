package com.itwray.iw.external.model.dto;

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
    private String phoneNumber;

    @NotBlank
    private String signName;

    @NotBlank
    private String templateCode;

    @NotBlank
    private String templateParam;
}
