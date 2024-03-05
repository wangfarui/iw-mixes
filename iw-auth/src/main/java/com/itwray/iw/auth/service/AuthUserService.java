package com.itwray.iw.auth.service;

import com.itwray.iw.auth.dto.LoginPasswordDto;
import com.itwray.iw.auth.dto.RegisterFormDto;
import com.itwray.iw.auth.vo.UserInfoVo;

/**
 * 用户接口服务
 *
 * @author wangfarui
 * @since 2024/3/2
 */
public interface AuthUserService {

    /**
     * 用户登录-通过密码方式登录
     *
     * @param dto 登录密码信息
     * @return 用户信息
     */
    UserInfoVo loginByPassword(LoginPasswordDto dto);

    /**
     * 用户注册-通过表单方式注册
     *
     * @param dto 用户注册信息
     */
    void registerByForm(RegisterFormDto dto);
}
