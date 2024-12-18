package com.itwray.iw.auth.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.RedisKeyConstants;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.auth.utils.SmsUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.redis.lock.DistributedLock;
import com.itwray.iw.web.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthRegisterServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Override
    @Transactional
    @DistributedLock(lockName = "'register:' + #dto.phoneNumber")
    public void registerByForm(RegisterFormDto dto, String clientIp) {
        // 校验同一ip的注册失败次数
        if (StringUtils.isNotBlank(clientIp)) {
            // 获取当前ip注册失败的次数
            Integer registerCount = RedisUtil.get(RedisKeyConstants.REGISTER_IP_KEY + clientIp, Integer.class);
            if (registerCount != null && registerCount > 5) {
                throw new BusinessException("注册频率太快，请稍后重试");
            }
        }

        // 获取电话号码验证码
        String phoneVerifyCode = RedisUtil.get(RedisKeyConstants.PHONE_VERIFY_KEY + dto.getPhoneNumber(), String.class);
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
        String oldVerificationCode = RedisUtil.get(RedisKeyConstants.PHONE_VERIFY_KEY + phoneNumber, String.class);
        if (oldVerificationCode != null) {
            throw new BusinessException("操作频繁，请稍后再试");
        }

        // 校验当前ip获取验证码的次数
        Integer verifyCount = RedisUtil.get(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, Integer.class);
        if (verifyCount != null && verifyCount >= 5) {
            throw new BusinessException("操作频繁，请稍后再试");
        }

        // 生成6位验证码
        Integer[] codes = NumberUtil.generateBySet(100000, 999999, 1);
        String verificationCode = codes[0].toString();
        // 同一号码, 2分钟内只发1次
        RedisUtil.set(RedisKeyConstants.PHONE_VERIFY_KEY + phoneNumber, verificationCode, 60 * 2);
        // 同一ip, 6小时内只发5次
        RedisUtil.increment(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, 1L);
        RedisUtil.expire(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, 60 * 60 * 6);

        // 发送短信
        SmsUtils.sendSms(phoneNumber, verificationCode);
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
