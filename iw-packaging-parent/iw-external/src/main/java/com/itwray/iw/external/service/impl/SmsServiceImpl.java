package com.itwray.iw.external.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.external.dao.SmsRecordsDao;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.external.model.entity.SmsRecordsEntity;
import com.itwray.iw.external.model.enums.SmsSendStatusEnum;
import com.itwray.iw.external.service.SmsService;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.enums.RuntimeEnvironmentEnum;
import com.itwray.iw.web.utils.EnvironmentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class SmsServiceImpl implements SmsService {

    private final SmsRecordsDao smsRecordsDao;

    /**
     * 运行环境
     */
    @Value("${iw.web.env:dev}")
    private RuntimeEnvironmentEnum env;

    /**
     * 短信服务Client
     */
    private static volatile Client client;

    /**
     * 短信服务Client锁
     */
    private static final Object CLIENT_LOCK = new Object();

    @Autowired
    public SmsServiceImpl(SmsRecordsDao smsRecordsDao) {
        this.smsRecordsDao = smsRecordsDao;
    }

    @Override
    public GeneralResponse<Void> sendVerificationCode(SmsSendVerificationCodeDto dto) {
        if (!RuntimeEnvironmentEnum.PROD.name().equals(env.name())) {
            log.info("非生产环境, 已跳过短信发送流程");
            return GeneralResponse.success();
        }
        // 保存发送短信记录实体
        SmsRecordsEntity smsRecordsEntity = SmsRecordsEntity.builder()
                .phoneNumber(dto.getPhoneNumber())
                .signName(dto.getSignName())
                .templateCode(dto.getTemplateCode())
                .templateParam(dto.getTemplateParam())
                .build();
        smsRecordsDao.save(smsRecordsEntity);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(dto.getPhoneNumber())
                .setSignName(dto.getSignName())
                .setTemplateCode(dto.getTemplateCode())
                .setTemplateParam(dto.getTemplateParam());
        try {
            SendSmsResponse sendSmsResponse = getClient().sendSms(sendSmsRequest);
            log.info("已执行短信发送操作, response: " + JSONUtil.toJsonStr(sendSmsResponse));
            if (!sendSmsResponse.getStatusCode().equals(GeneralApiCode.SUCCESS.getCode())) {
                return GeneralResponse.fail("短信发送失败，请重试");
            }
            if (!"OK".equals(sendSmsResponse.getBody().getCode())) {
                return GeneralResponse.fail("短信发送失败，请重试");
            }
            smsRecordsDao.updateSendStatus(smsRecordsEntity.getId(), SmsSendStatusEnum.SUCCESS);
        } catch (Exception e) {
            log.error("短信发送异常", e);
            smsRecordsDao.updateSendStatus(smsRecordsEntity.getId(), SmsSendStatusEnum.FAIL);
            if (e instanceof BusinessException businessException) {
                throw businessException;
            } else {
                throw new BusinessException("短信发送失败，请稍后重试");
            }
        }
        return GeneralResponse.success();
    }

    public Client getClient() {
        if (client == null) {
            synchronized (CLIENT_LOCK) {
                if (client == null) {
                    Config config = new Config()
                            .setAccessKeyId(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-id"))
                            .setAccessKeySecret(EnvironmentHolder.getRequiredProperty("aliyun.sms.access-key-secret"))
                            .setEndpoint(EnvironmentHolder.getProperty("aliyun.sms.endpoint", "dysmsapi.aliyuncs.com"));
                    try {
                        client = new Client(config);
                    } catch (Exception e) {
                        throw new IwWebException(e);
                    }
                }
            }
        }
        return client;
    }
}
