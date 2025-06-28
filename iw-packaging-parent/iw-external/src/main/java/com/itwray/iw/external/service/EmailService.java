package com.itwray.iw.external.service;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.external.model.dto.SendEmailDto;

/**
 * 邮件服务 接口服务
 *
 * @author farui.wang
 * @since 2025/5/28
 */
public interface EmailService {

    /**
     * 发送单条邮件
     */
    GeneralResponse<Void> sendSingleEmail(SendEmailDto dto);
}
