package com.itwray.iw.auth.service.impl;

import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.auth.service.AuthVerificationService;
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

    private AuthVerificationService authVerificationService;

    @Autowired
    public AuthRegisterServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Autowired
    public void setAuthVerificationService(AuthVerificationService authVerificationService) {
        this.authVerificationService = authVerificationService;
    }

    @Override
    @Transactional
    @DistributedLock(lockName = "'register:' + #dto.phoneNumber")
    public UserInfoVo registerByForm(RegisterFormDto dto, String clientIp) {
        // 校验同一ip的注册失败次数
        if (StringUtils.isNotBlank(clientIp)) {
            // 获取当前ip注册失败的次数
            Integer registerCount = RedisUtil.get(AuthRedisKeyEnum.REGISTER_IP_KEY.getKey(clientIp), Integer.class);
            if (registerCount != null && registerCount > 5) {
                throw new BusinessException("注册频率太快，请稍后重试");
            }
        }

        // 获取电话号码验证码
        String phoneVerifyCode = RedisUtil.get(AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY.getKey(dto.getPhoneNumber()), String.class);
        if (phoneVerifyCode == null) {
            throw new BusinessException("验证码失效，请重新获取");
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
        AuthUserEntity authUserEntity = authUserDao.addNewUser(userAddBo);

        return authUserDao.loginSuccessAfter(authUserEntity);
    }

    /**
     * 根据电话号码生成验证码
     * <p>暂不受登录注册影响</p>
     *
     * @param phoneNumber 电话号码
     */
    @Override
    public void getPhoneVerificationCode(String phoneNumber) {
        authVerificationService.getPhoneVerificationCode(phoneNumber, AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY);
    }

    /**
     * 用户登录/注册时获取邮箱验证码
     *
     * @param emailAddress 邮箱地址
     */
    @Override
    public void getEmailVerificationCode(String emailAddress) {
        authVerificationService.getEmailVerificationCode(emailAddress, AuthRedisKeyEnum.USER_LOGIN_EMAIL_VERIFY_KEY);
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
