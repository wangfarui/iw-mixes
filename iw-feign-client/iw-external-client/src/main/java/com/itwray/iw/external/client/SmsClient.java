package com.itwray.iw.external.client;

import com.itwray.iw.external.core.InternalFeignConfig;
import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SMS短信 Client
 *
 * @author wray
 * @since 2024/12/19
 */
@Validated
@FeignClient(value = ExternalClientConstants.SERVICE_NAME, contextId = "sms-client", path = ExternalClientConstants.INTERNAL_SERVICE_PATH + "/sms", configuration = InternalFeignConfig.class)
public interface SmsClient {

    @PostMapping("/sendVerificationCode")
    @Operation(summary = "发送验证码")
    void sendVerificationCode(@RequestBody @Valid SmsSendVerificationCodeDto dto);
}

