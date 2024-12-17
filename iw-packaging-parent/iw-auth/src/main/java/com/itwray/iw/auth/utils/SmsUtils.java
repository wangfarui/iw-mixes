package com.itwray.iw.auth.utils;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.web.core.EnvironmentHolder;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.enums.RuntimeEnvironmentEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * SMS短信服务工具类
 *
 * @author wray
 * @since 2024/12/16
 */
@Slf4j
public abstract class SmsUtils {

    /**
     * 短信服务Client
     */
    private static volatile Client client;

    /**
     * 签名名称
     */
    private static volatile String signName;

    /**
     * 模板CODE
     */
    private static volatile String templateCode;

    /**
     * 运行环境
     */
    private static volatile RuntimeEnvironmentEnum env;

    /**
     * 发送授权验证码短信
     */
    public static void sendSms(String phone, String code) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(SmsUtils.getSignName())
                .setTemplateCode(SmsUtils.getTemplateCode())
                .setTemplateParam("{\"code\":\"" + code + "\"}");

        SmsUtils.sendSms(sendSmsRequest);
    }

    /**
     * 发送短信
     */
    public static void sendSms(SendSmsRequest sendSmsRequest) {
        if (!RuntimeEnvironmentEnum.PROD.name().equals(SmsUtils.getEnv().name())) {
            log.info("非生产环境, 已跳过短信发送流程");
            return;
        }
        try {
            SendSmsResponse sendSmsResponse = getClient().sendSms(sendSmsRequest);
            if (!sendSmsResponse.getStatusCode().equals(GeneralApiCode.SUCCESS.getCode())) {
                throw new BusinessException("短信发送失败，请重试");
            }
            if (!"OK".equals(sendSmsResponse.getBody().getCode())) {
                throw new BusinessException("短信发送失败，请重试");
            }
            log.info("短信发送成功, response: " + JSONUtil.toJsonStr(sendSmsResponse));
        } catch (Exception e) {
            log.error("发送短信异常", e);
            throw new BusinessException("短信发送失败，请稍后重试");
        }

    }

    public static Client getClient() throws Exception {
        if (client == null) {
            synchronized (SmsUtils.class) {
                if (client == null) {
                    Config config = new Config()
                            .setAccessKeyId(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-id"))
                            .setAccessKeySecret(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-secret"))
                            .setEndpoint(EnvironmentHolder.getProperty("aliyun.sms.endpoint", "dysmsapi.aliyuncs.com"));

                    client = new Client(config);
                }
            }
        }
        return client;
    }

    public static String getSignName() {
        if (signName == null) {
            synchronized (SmsUtils.class) {
                if (signName == null) {
                    signName = EnvironmentHolder.getRequiredProperty("aliyun.sms.sign-name");
                }
            }
        }
        return signName;
    }

    public static String getTemplateCode() {
        if (templateCode == null) {
            synchronized (SmsUtils.class) {
                if (templateCode == null) {
                    templateCode = EnvironmentHolder.getRequiredProperty("aliyun.sms.template-code");
                }
            }
        }
        return templateCode;
    }

    public static RuntimeEnvironmentEnum getEnv() {
        if (env == null) {
            synchronized (SmsUtils.class) {
                if (env == null) {
                    env = EnvironmentHolder.getProperty("iw.web.env", RuntimeEnvironmentEnum.class, RuntimeEnvironmentEnum.DEV);
                }
            }
        }
        return env;
    }
}
