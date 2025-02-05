package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.LoginVerificationCodeDto;
import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;

/**
 * 用户服务接口
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
     * 用户登录-通过验证码校验方式登录
     *
     * @param dto 登录验证码信息
     * @return 用户信息
     */
    UserInfoVo loginByVerificationCode(LoginVerificationCodeDto dto);

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

    /**
     * 修改密码
     *
     * @param dto 用户密码信息
     */
    void editPassword(UserPasswordEditDto dto);

    /**
     * 根据操作行为获取验证码
     *
     * @param action   操作行为{@link com.itwray.iw.auth.model.enums.VerificationCodeActionEnum}
     * @param clientIp 客户端ip
     */
    void getVerificationCodeByAction(Integer action, String clientIp);
}
