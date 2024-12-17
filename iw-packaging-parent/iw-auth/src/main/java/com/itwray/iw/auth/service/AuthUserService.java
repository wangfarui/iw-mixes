package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;

/**
 * 用户接口服务
 *
 * @author wray
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
     * 退出登录
     */
    void logout();

    /**
     * 校验Token有效性
     *
     * @param token Token
     * @return ture -> 有效
     */
    Boolean validateToken(String token);

    /**
     * 获取指定token的用户id
     *
     * @param token Token
     * @return 用户id
     */
    Integer getUserId(String token);

    /**
     * 修改头像
     *
     * @param avatar 头像地址
     */
    void editAvatar(String avatar);
}
