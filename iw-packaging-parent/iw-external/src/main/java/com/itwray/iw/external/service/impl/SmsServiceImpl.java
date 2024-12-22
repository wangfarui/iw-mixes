package com.itwray.iw.external.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.external.service.SmsService;
import com.itwray.iw.web.utils.EnvironmentHolder;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.enums.RuntimeEnvironmentEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * SMS短信服务实现层
 *
 * @author wray
 * @since 2024/12/19
 */
@Service
@Slf4j
@RefreshScope
public class SmsServiceImpl implements SmsService, ApplicationRunner {

    /**
     * 短信服务Client
     */
    private Client client;

    /**
     * 运行环境
     */
    @Value("${iw.web.env:dev}")
    private RuntimeEnvironmentEnum env;

    @Override
    public void sendVerificationCode(SmsSendVerificationCodeDto dto) {
        if (!RuntimeEnvironmentEnum.PROD.name().equals(env.name())) {
            log.info("非生产环境, 已跳过短信发送流程");
            return;
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(dto.getPhoneNumber())
                .setSignName(dto.getSignName())
                .setTemplateCode(dto.getTemplateCode())
                .setTemplateParam(dto.getTemplateParam());
        try {
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            if (!sendSmsResponse.getStatusCode().equals(GeneralApiCode.SUCCESS.getCode())) {
                throw new BusinessException("短信发送失败，请重试");
            }
            if (!"OK".equals(sendSmsResponse.getBody().getCode())) {
                throw new BusinessException("短信发送失败，请重试");
            }
            log.info("短信发送成功, response: " + JSONUtil.toJsonStr(sendSmsResponse));
        } catch (Exception e) {
            log.error("短信发送异常", e);
            throw new BusinessException("短信发送失败，请稍后重试");
        }
    }

    /**
     * 应用启动后，初始化连接sms客户端
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Config config = new Config()
                .setAccessKeyId(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-id"))
                .setAccessKeySecret(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-secret"))
                .setEndpoint(EnvironmentHolder.getProperty("aliyun.sms.endpoint", "dysmsapi.aliyuncs.com"));

        client = new Client(config);
    }
}
