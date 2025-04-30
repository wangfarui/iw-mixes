package com.itwray.iw.auth.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.service.AuthVerificationService;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.external.client.SmsClient;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.starter.redis.RedisKeyManager;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.redis.lock.DistributedLock;
import com.itwray.iw.web.config.IwAliyunProperties;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.utils.IpUtils;
import com.itwray.iw.web.utils.SpringWebHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 授权验证服务实现层
 *
 * @author wray
 * @since 2025/3/11
 */
@Service
@Slf4j
public class AuthVerificationServiceImpl implements AuthVerificationService {

    private SmsClient smsClient;

    private IwAliyunProperties iwAliyunProperties;

    // SmsClient 统一由 web 模块扫描注册为Bean对象
    @SuppressWarnings("all")
    @Autowired
    public void setSmsClient(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @Autowired
    public void setIwAliyunProperties(IwAliyunProperties iwAliyunProperties) {
        this.iwAliyunProperties = iwAliyunProperties;
    }

    @Override
    @DistributedLock(lockName = "'getVerificationCode:' + #phoneNumber")
    public String getVerificationCode(String phoneNumber, RedisKeyManager keyManager) {
        if (!NumberUtils.isValidPhoneNumber(phoneNumber)) {
            throw new BusinessException("电话号码格式错误");
        }

        String clientIp = IpUtils.getClientIp(SpringWebHolder.getRequest());

        // 查询当前电话号码是否已生成过验证码
        String oldVerificationCode = RedisUtil.get(keyManager.getKey(phoneNumber), String.class);
        if (oldVerificationCode != null) {
            // 如果缓存中存在验证码，则表示短时间内已发送过验证码，直接返回
            return oldVerificationCode;
        }

        // 校验当前ip获取验证码的次数
        Integer verifyCount = RedisUtil.get(AuthRedisKeyEnum.PHONE_VERIFY_IP_KEY.getKey(clientIp), Integer.class);
        if (verifyCount != null && verifyCount >= 5) {
            throw new BusinessException("操作频繁，请稍后再试");
        }

        // 生成6位验证码
        Integer[] codes = NumberUtil.generateBySet(100000, 999999, 1);
        String verificationCode = codes[0].toString();

        // 构建发送短信验证码对象
        SmsSendVerificationCodeDto dto = new SmsSendVerificationCodeDto();
        dto.setPhoneNumber(phoneNumber);
        dto.setSignName(this.iwAliyunProperties.getSms().getSignName());
        dto.setTemplateCode(this.iwAliyunProperties.getSms().getTemplateCode());
        dto.setTemplateParam("{\"code\":\"" + verificationCode + "\"}");
        // 同步调用发送验证码
        smsClient.sendVerificationCode(dto);

        // 同一号码, 验证码5分钟内有效，5分钟内重复发送则覆盖
        keyManager.setStringValue(verificationCode, phoneNumber);
        // 同一ip, 1小时内只发5次
        RedisUtil.incrementOne(AuthRedisKeyEnum.PHONE_VERIFY_IP_KEY.getKey(clientIp));
        AuthRedisKeyEnum.PHONE_VERIFY_IP_KEY.setExpire(clientIp);

        return verificationCode;
    }

    @Override
    public boolean compareVerificationCode(String verificationCode, String phoneNumber, RedisKeyManager keyManager) {
        if (StringUtils.isBlank(verificationCode)) {
            return false;
        }
        // 获取电话号码验证码
        String phoneVerifyCode = RedisUtil.get(keyManager.getKey(phoneNumber), String.class);
        if (StringUtils.isBlank(phoneVerifyCode)) {
            throw new BusinessException("验证码已失效，请重新获取");
        }

        // 比对验证码是否正确
        boolean res = phoneVerifyCode.equals(verificationCode);

        // 验证码验证通过后 即失效
        if (res) {
            RedisUtil.delete(keyManager.getKey(phoneNumber));
        }

        return res;
    }
}
