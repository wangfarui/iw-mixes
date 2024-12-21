package com.itwray.iw.external.controller;

import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.external.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SMS短信接口服务
 *
 * @author wray
 * @since 2024/12/20
 */
@RestController
@RequestMapping(ExternalClientConstants.INTERNAL_PATH_PREFIX + "/sms")
@Validated
@Tag(name = "SMS短信服务接口（内部服务使用）")
public class SmsController {

    private final SmsService smsService;

    @Autowired
    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/sendVerificationCode")
    @Operation(summary = "发送验证码")
    public void sendVerificationCode(@RequestBody @Valid SmsSendVerificationCodeDto dto) {
        smsService.sendVerificationCode(dto);
    }
}
