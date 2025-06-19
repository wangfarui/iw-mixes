package com.itwray.iw.external.client;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.external.core.InternalFeignConfig;
import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.dto.SendEmailDto;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 内部接口服务 Client
 *
 * @author wray
 * @since 2025/4/12
 */
@Validated
@FeignClient(value = ExternalClientConstants.SERVICE_NAME, contextId = "internal-client", path = ExternalClientConstants.INTERNAL_SERVICE_PATH, configuration = InternalFeignConfig.class)
public interface InternalApiClient {

    @PostMapping("/api/getExchangeRate")
    @Operation(summary = "查询汇率")
    Object getExchangeRate(@RequestBody @Valid GetExchangeRateDto dto);

    @PostMapping("/sms/sendVerificationCode")
    @Operation(summary = "发送验证码")
    GeneralResponse<Void> sendVerificationCode(@RequestBody @Valid SmsSendVerificationCodeDto dto);

    @PostMapping("/email/sendSingleEmail")
    @Operation(summary = "发送单个邮件")
    GeneralResponse<Void> sendSingleEmail(@RequestBody @Valid SendEmailDto dto);

    @GetMapping("/ai/answer")
    GeneralResponse<String> aiAnswer(@RequestParam("t") String content);
}
