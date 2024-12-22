package com.itwray.iw.auth.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.external.client.SmsClient;
import com.itwray.iw.external.model.dto.SmsSendVerificationCodeDto;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.redis.lock.DistributedLock;
import com.itwray.iw.web.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 授权注册服务实现层
 *
 * @author wray
 * @since 2024/12/16
 */
@Service
public class AuthRegisterServiceImpl implements AuthRegisterService {

    private final AuthUserDao authUserDao;

    private SmsClient smsClient;

    /**
     * 签名名称
     */
    @Value("${aliyun.sms.sign-name}")
    private String signName;

    /**
     * 模板CODE
     */
    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    @Autowired
    public AuthRegisterServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Autowired
    public void setSmsClient(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @Override
    @Transactional
    @DistributedLock(lockName = "'register:' + #dto.phoneNumber")
    public void registerByForm(RegisterFormDto dto, String clientIp) {
        // 校验同一ip的注册失败次数
        if (StringUtils.isNotBlank(clientIp)) {
            // 获取当前ip注册失败的次数
            Integer registerCount = RedisUtil.get(AuthRedisKeyEnum.REGISTER_IP_KEY.getKey(clientIp), Integer.class);
            if (registerCount != null && registerCount > 5) {
                throw new BusinessException("注册频率太快，请稍后重试");
            }
        }

        // 获取电话号码验证码
        String phoneVerifyCode = RedisUtil.get(AuthRedisKeyEnum.PHONE_VERIFY_KEY.getKey(dto.getPhoneNumber()), String.class);
        if (phoneVerifyCode == null) {
            throw new BusinessException("验证码已失效，请重新获取");
        }
        // 比对验证码是否正确
        if (!dto.getVerificationCode().equals(phoneVerifyCode)) {
            // 为防止用户恶意猜测验证码，增加验证次数限制
            authUserDao.incrementClientIpLockCount(clientIp);
            throw new BusinessException("验证码错误，请重新输入");
        }

        // 新增用户
        UserAddBo userAddBo = new UserAddBo();
        BeanUtils.copyProperties(dto, userAddBo);
        authUserDao.addNewUser(userAddBo);
    }

    /**
     * 根据电话号码生成验证码
     * <p>暂不受登录注册影响</p>
     *
     * @param phoneNumber 电话号码
     * @param clientIp    客户端请求ip
     * @return 生成验证码结果
     */
    @Override
    @DistributedLock(lockName = "'getVerificationCode:' + #phoneNumber")
    public void getVerificationCode(String phoneNumber, String clientIp) {
        // 查询当前电话号码是否已生成过验证码
        String oldVerificationCode = RedisUtil.get(AuthRedisKeyEnum.PHONE_VERIFY_KEY.getKey(phoneNumber), String.class);
        if (oldVerificationCode != null) {
            // 如果缓存中存在验证码，则表示短时间内已发送过验证码，直接返回成功
            return;
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
        dto.setSignName(this.signName);
        dto.setTemplateCode(this.templateCode);
        dto.setTemplateParam("{\"code\":\"" + verificationCode + "\"}");
        // 同步调用发送验证码
        smsClient.sendVerificationCode(dto);

        // 同一号码, 验证码5分钟内有效，5分钟内重复发送则覆盖
        RedisUtil.set(AuthRedisKeyEnum.PHONE_VERIFY_KEY.getKey(phoneNumber), verificationCode, 60 * 5);
        // 同一ip, 1小时内只发5次
        RedisUtil.increment(AuthRedisKeyEnum.PHONE_VERIFY_IP_KEY.getKey(clientIp), 1L);
        RedisUtil.expire(AuthRedisKeyEnum.PHONE_VERIFY_IP_KEY.getKey(clientIp), 60 * 60);
    }


    /**
     * 校验用户是否唯一
     *
     * @param phoneNumber 唯一电话号码
     * @param username    唯一用户名
     * @param clientIp    客户端ip
     */
    private void checkUserUnique(@Nullable String phoneNumber, @Nullable String username, String clientIp) {
        // 校验电话号码是否已注册
        if (StringUtils.isNotBlank(phoneNumber)) {
            AuthUserEntity authUserEntity = authUserDao.queryOneByPhoneNumber(phoneNumber);
            if (authUserEntity != null) {
                // 为防止用户恶意猜测电话号码，增加注册次数限制
                authUserDao.incrementClientIpLockCount(clientIp);
                throw new BusinessException("用户已存在");
            }
        }

        // 校验用户名是否已注册
        if (StringUtils.isNotBlank(username)) {
            AuthUserEntity authUserEntity = authUserDao.queryOneByUsername(username);
            if (authUserEntity != null) {
                // 为防止用户恶意猜测用户名，增加注册次数限制
                authUserDao.incrementClientIpLockCount(clientIp);
                throw new BusinessException("用户已存在");
            }
        }
    }
}
