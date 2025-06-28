package com.itwray.iw.auth.service;

import com.itwray.iw.starter.redis.RedisKeyManager;

/**
 * 授权验证服务
 *
 * @author wray
 * @since 2025/3/11
 */
public interface AuthVerificationService {

    /**
     * 获取电话号码的验证码
     * <p>必须得是 Web 请求!!!</p>
     *
     * @param phoneNumber 电话号码
     * @param keyManager  Redis Key Manager
     * @return 验证码
     */
    String getPhoneVerificationCode(String phoneNumber, RedisKeyManager keyManager);

    /**
     * 比对验证码是否正确
     *
     * @param verificationCode 待比对的验证码
     * @param account          Redis Key 的账号
     * @param keyManager       Redis Key Manager
     * @return true -> 验证码正确
     */
    boolean compareVerificationCode(String verificationCode, String account, RedisKeyManager keyManager);

    /**
     * 获取邮箱的验证码
     * <p>必须得是 Web 请求!!!</p>
     *
     * @param emailAddress 邮箱地址
     * @param keyManager   Redis Key Manager
     * @return 验证码
     */
    String getEmailVerificationCode(String emailAddress, RedisKeyManager keyManager);
}
