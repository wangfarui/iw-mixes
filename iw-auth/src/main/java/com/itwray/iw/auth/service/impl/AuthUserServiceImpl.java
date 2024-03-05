package com.itwray.iw.auth.service.impl;

import com.itwray.iw.auth.core.AuthServiceException;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthUser;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (authUser == null) {
            // 为防止用户恶意猜测账号，异常信息同密码错误一样
            throw new AuthServiceException("账号或密码错误");
        }
        if (!authUser.getPassword().equals(dto.getPassword())) {
            throw new AuthServiceException("账号或密码错误");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(authUser, userInfoVo);
        return userInfoVo;
    }

    @Override
    @Transactional
    public void registerByForm(RegisterFormDto dto) {
        // 校验账号是否已注册
        AuthUser authUser = authUserDao.queryOneByAccount(dto.getAccount());
        if (authUser != null) {
            // TODO 为防止用户恶意猜测账号，需要增加注册次数限制
            throw new AuthServiceException("账号已注册");
        }
        AuthUser addUser = new AuthUser();
        BeanUtils.copyProperties(dto, addUser);
        authUserDao.save(addUser);
    }
}
