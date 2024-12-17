package com.itwray.iw.auth.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.RedisKeyConstants;
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
    @DistributedLock(lockName = "'register:' + #dto.username")
    public void registerByForm(RegisterFormDto dto, String clientIp) {
        if (StringUtils.isNotBlank(clientIp)) {
            // 获取当前ip注册失败的次数
            Long registerCount = RedisUtil.get(RedisKeyConstants.REGISTER_IP_KEY + clientIp, Long.class);
            if (registerCount != null && registerCount > 5) {
                throw new BusinessException("注册频率太快，请稍后重试");
            }
        }
        // 校验用户名是否已注册
        AuthUserEntity authUserEntity = authUserDao.queryOneByUsername(dto.getUsername());
        if (authUserEntity != null) {
            // 为防止用户恶意猜测用户名，需要增加注册次数限制
            RedisUtil.increment(RedisKeyConstants.REGISTER_IP_KEY + clientIp, 1L);
            RedisUtil.expire(RedisKeyConstants.REGISTER_IP_KEY + clientIp, 60 * 60);
            throw new BusinessException("用户名已注册");
        }
        AuthUserEntity addUser = new AuthUserEntity();
        BeanUtils.copyProperties(dto, addUser);
        // 姓名为空时，默认使用用户名作为姓名
        if (StrUtil.isBlank(addUser.getName())) {
            addUser.setName(addUser.getUsername());
        }
        // 密码加密存储
        addUser.setPassword(BCrypt.hashpw((addUser.getPassword())));
        authUserDao.save(addUser);
    }

    @Override
    @DistributedLock(lockName = "'getVerificationCode:' + #phoneNumber")
    public String getVerificationCode(String phoneNumber, String clientIp) {
        // 查询当前电话号码是否已生成过验证码
        String oldVerificationCode = RedisUtil.get(RedisKeyConstants.PHONE_VERIFY_KEY + phoneNumber, String.class);
        if (oldVerificationCode != null) {
            return "操作频繁，请稍后再试";
        }

        // 校验当前ip获取验证码的次数
        Integer verifyCount = RedisUtil.get(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, Integer.class);
        if (verifyCount != null && verifyCount >= 5) {
            return "操作频繁，请稍后再试";
        }

        // 生成6位验证码
        Integer[] codes = NumberUtil.generateBySet(100000, 999999, 1);
        String verificationCode = codes[0].toString();
        // 同一号码, 2分钟内只发1次
        RedisUtil.set(RedisKeyConstants.PHONE_VERIFY_KEY + phoneNumber, verificationCode, 60 * 2);
        RedisUtil.increment(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, 1L);
        // 同一ip, 6小时内只发5次
        RedisUtil.expire(RedisKeyConstants.PHONE_VERIFY_IP_KEY + clientIp, 60 * 60 * 6);

        // 发送短信
        SmsUtils.sendSms(phoneNumber, verificationCode);

        return "验证码已发送";
    }
}
