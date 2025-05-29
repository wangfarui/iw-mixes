package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;

/**
 * 授权注册服务
 *
 * @author wray
 * @since 2024/12/16
 */
public interface AuthRegisterService {

    /**
     * 用户注册-通过表单方式注册
     *
     * @param dto      用户注册信息
     * @param clientIp 客户端ip
     * @return 用户信息
     */
    UserInfoVo registerByForm(RegisterFormDto dto, String clientIp);

    /**
     * 用户登录/注册时获取短信验证码
     *
     * @param phoneNumber 电话号码
     */
    void getPhoneVerificationCode(String phoneNumber);

    /**
     * 用户登录/注册时获取邮箱验证码
     *
     * @param emailAddress 邮箱地址
     */
    void getEmailVerificationCode(String emailAddress);
}
