package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.dto.UserLoginDto;
import com.itwray.iw.eat.model.vo.UserLoginVo;

/**
 * 用户接口服务
 *
 * @author wray
 * @since 2024/4/29
 */
public interface EatUserService {

    /**
     * 登录
     */
    UserLoginVo doLogin(UserLoginDto dto);
}
