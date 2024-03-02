package com.itwray.iw.auth.service.impl;

import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.dto.LoginPasswordDto;
import com.itwray.iw.auth.entity.AuthUser;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.auth.vo.UserInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现层
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserDao authUserDao;

    @Override
    public UserInfoVo loginByPassword(LoginPasswordDto dto) {
        AuthUser authUser = authUserDao.queryOneByAccount(dto.getAccount());
        if (!authUser.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(authUser, userInfoVo);
        return userInfoVo;
    }
}
