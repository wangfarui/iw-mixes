package com.itwray.iw.auth.service;

import com.itwray.iw.auth.dto.LoginPasswordDto;
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
}
