package com.itwray.iw.eat.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.eat.dao.EatUserDao;
import com.itwray.iw.eat.model.dto.UserLoginDto;
import com.itwray.iw.eat.model.entity.EatUserEntity;
import com.itwray.iw.eat.model.vo.UserLoginVo;
import com.itwray.iw.eat.service.EatUserService;
import com.itwray.iw.web.exception.IwWebException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现层
 *
 * @author wray
 * @since 2024/4/29
 */
@Service
public class EatUserServiceImpl implements EatUserService {

    @Resource
    private EatUserDao eatUserDao;

    @Override
    public UserLoginVo doLogin(UserLoginDto dto) {
        EatUserEntity userEntity = eatUserDao.lambdaQuery()
                .eq(EatUserEntity::getUsername, dto.getUsername())
                .last("limit 1")
                .one();
        if (userEntity == null) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new IwWebException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(dto.getPassword(), userEntity.getPassword())) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new IwWebException("用户名或密码错误");
        }
        StpUtil.login(userEntity.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return new UserLoginVo(userEntity.getName(), tokenInfo.getTokenName(), tokenInfo.getTokenValue());
    }

    public static void main(String[] args) {
        String hashpw = BCrypt.hashpw("123456");
        System.out.println(hashpw);
    }
}
