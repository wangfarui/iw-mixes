package com.itwray.iw.external.service;

import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;

/**
 * 短信接口服务
 *
 * @author wray
 * @since 2024/12/19
 */
public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param dto 验证码对象
     */
    void sendVerificationCode(SmsSendVerificationCodeDto dto);
}
