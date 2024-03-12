package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.itwray.iw.auth.core.AuthServiceException;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthUserDetails;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthRole;
import com.itwray.iw.auth.model.entity.AuthUser;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现层
 *
 * @author wray
 * @since 2024/3/2
 */
@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements UserDetailsService, AuthUserService {

    private final AuthUserDao authUserDao;

    /**
     * 通过 {@link org.springframework.security.config.annotation.web.builders.HttpSecurity#formLogin} 方式访问
     *
     * @param username 用户名
     * @return 用户详情信息
     */
    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.queryOneByUsername(username);
        if (authUser == null) {
            throw new AuthServiceException("用户名不存在");
        }
        List<AuthRole> authRoles = authUserDao.getBaseMapper().queryRolesByUserId(authUser.getId());
        AuthUserDetails authUserDetails = BeanUtil.copyProperties(authUser, AuthUserDetails.class);
        authUserDetails.setRoles(authRoles);
        return authUserDetails;
    }

    /**
     * 通过自定义Web接口访问
     *
     * @param dto 登录密码信息
     * @return 用户登录信息
     */
    @Override
    public UserInfoVo loginByPassword(LoginPasswordDto dto) {
        AuthUser authUser = authUserDao.queryOneByUsername(dto.getUsername());
        if (authUser == null) {
            // 为防止用户恶意猜测用户名，异常信息同密码错误一样
            throw new AuthServiceException("用户名或密码错误");
        }
        if (!authUser.getPassword().equals(dto.getPassword())) {
            throw new AuthServiceException("用户名或密码错误");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(authUser, userInfoVo);
        return userInfoVo;
    }

    @Override
    @Transactional
    public void registerByForm(RegisterFormDto dto) {
        // 校验用户名是否已注册
        AuthUser authUser = authUserDao.queryOneByUsername(dto.getUsername());
        if (authUser != null) {
            // TODO 为防止用户恶意猜测用户名，需要增加注册次数限制
            throw new AuthServiceException("用户名已注册");
        }
        AuthUser addUser = new AuthUser();
        BeanUtils.copyProperties(dto, addUser);
        // 姓名为空时，默认使用用户名作为姓名
        if (StrUtil.isBlank(addUser.getName())) {
            addUser.setName(addUser.getUsername());
        }
        authUserDao.save(addUser);
    }
}
