package com.itwray.iw.external.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.dto.SendEmailDto;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.external.model.vo.GetExchangeRateVo;
import com.itwray.iw.external.service.EmailService;
import com.itwray.iw.external.service.InternalApiService;
import com.itwray.iw.external.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部API接口
 *
 * @author wray
 * @since 2025/4/12
 */
@RestController
@RequestMapping(ExternalClientConstants.INTERNAL_PATH_PREFIX)
@Tag(name = "内部API接口（内部服务使用）")
public class InternalApiController {

    private final InternalApiService internalApiService;

    private final SmsService smsService;

    private final EmailService emailService;

    public InternalApiController(InternalApiService internalApiService,
                                 SmsService smsService,
                                 EmailService emailService) {
        this.internalApiService = internalApiService;
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @PostMapping("/api/getExchangeRate")
    @Operation(summary = "查询汇率")
    public GetExchangeRateVo getExchangeRate(@RequestBody @Valid GetExchangeRateDto dto) {
        return internalApiService.getExchangeRate(dto);
    }

    @PostMapping("/sms/sendVerificationCode")
    @Operation(summary = "发送验证码")
    public GeneralResponse<Void> sendVerificationCode(@RequestBody @Valid SmsSendVerificationCodeDto dto) {
        return smsService.sendVerificationCode(dto);
    }

    @PostMapping("/email/sendSingleEmail")
    @Operation(summary = "发送单个邮件")
    public GeneralResponse<Void> sendSingleEmail(@RequestBody @Valid SendEmailDto dto) {
        return emailService.sendSingleEmail(dto);
    }
}
