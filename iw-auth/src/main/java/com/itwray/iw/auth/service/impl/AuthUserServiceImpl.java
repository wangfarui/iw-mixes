package com.itwray.iw.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.core.AuthServiceException;
import com.itwray.iw.auth.core.SpringWebHolder;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.IwWebException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 用户服务实现层
 *
 * @author wray
 * @since 2024/3/2
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserDao authUserDao;

    /**
     * token固定的存活时间 3天
     */
    private static final Long ACTIVE_TIME = 3 * 24 * 60 * 60L;

    /**
     * token的固定header
     */
    private static final String TOKEN_HEADER = "iwtoken";

    @Autowired
    public AuthUserServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    /**
     * 通过自定义Web接口访问
     *
     * @param dto 登录密码信息
     * @return 用户登录信息
     */
    @Override
    public UserInfoVo loginByPassword(LoginPasswordDto dto) {
        AuthUserEntity authUserEntity = authUserDao.queryOneByUsername(dto.getUsername());
        if (authUserEntity == null) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 为防止用户恶意猜测用户名，异常信息同密码错误一样
            throw new AuthServiceException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(dto.getPassword(), authUserEntity.getPassword())) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new IwWebException("用户名或密码错误");
        }

        // 生成Token并缓存
        String token = UUID.randomUUID().toString();
        RedisUtil.set(token, authUserEntity.getId(), ACTIVE_TIME);

        // 将token写入到请求头中
        this.setTokenValue(token);

        // 构建响应对象
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setName(authUserEntity.getName());
        userInfoVo.setAvatar(authUserEntity.getAvatar());
        userInfoVo.setTokenName(TOKEN_HEADER);
        userInfoVo.setTokenValue(token);

        return userInfoVo;
    }

    @Override
    public void logout() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        String token = request.getHeader(TOKEN_HEADER);
        if (token == null) {
            return;
        }
        // 移除token缓存
        RedisUtil.delete(token);
    }

    @Override
    @Transactional
    public void registerByForm(RegisterFormDto dto) {
        // 校验用户名是否已注册
        AuthUserEntity authUserEntity = authUserDao.queryOneByUsername(dto.getUsername());
        if (authUserEntity != null) {
            // TODO 为防止用户恶意猜测用户名，需要增加注册次数限制
            throw new AuthServiceException("用户名已注册");
        }
        AuthUserEntity addUser = new AuthUserEntity();
        BeanUtils.copyProperties(dto, addUser);
        // 姓名为空时，默认使用用户名作为姓名
        if (StrUtil.isBlank(addUser.getName())) {
            addUser.setName(addUser.getUsername());
        }
        // 密码加密存储
        addUser.setPassword(BCrypt.hashpw((addUser.getPassword())));
        authUserDao.save(addUser);
    }

    /**
     * 校验token有效性(默认续签)
     *
     * @param token   token值
     * @return true -> 有效
     */
    @Override
    public Boolean validateToken(String token) {
        return this.validateToken(token, true);
    }

    /**
     * 校验token有效性
     *
     * @param token   token值
     * @param isRenew 是否续签
     * @return true -> 有效
     */
    public Boolean validateToken(String token, boolean isRenew) {
        // token不存在 或者 token过期自动删除
        if (!RedisUtil.hasKey(token)) {
            return false;
        }

        // 自动续签
        if (isRenew) {
            RedisUtil.expire(token, ACTIVE_TIME);
        }

        return true;
    }

    @Override
    @Transactional
    public void editAvatar(String avatar) {
        Integer loginId = this.getLoginId();
        AuthUserEntity userEntity = authUserDao.getById(loginId);
        if (userEntity == null) {
            throw new AuthorizedException("用户不存在，请重新登录");
        }
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, loginId)
                .set(AuthUserEntity::getAvatar, avatar)
                .update();
    }

    /**
     * 存放token至请求头中
     *
     * @param tokenValue token值
     */
    private void setTokenValue(String tokenValue) {
        HttpServletResponse response = SpringWebHolder.getResponse();
        response.setHeader(TOKEN_HEADER, tokenValue);
        // 此处必须在响应头里指定 Access-Control-Expose-Headers: token-name，否则前端无法读取到这个响应头
        response.addHeader("Access-Control-Expose-Headers", TOKEN_HEADER);
    }

    /**
     * 获取当前登录用户的id
     */
    private Integer getLoginId() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        String token = request.getHeader(TOKEN_HEADER);
        if (token == null) {
            throw new AuthorizedException("当前未登录，请先登录");
        }
        Boolean validity = this.validateToken(token, false);
        if (!validity) {
            throw new AuthorizedException("登录状态已失效，请重新登录");
        }
        Object userId = RedisUtil.get(token);
        if (userId == null) {
            throw new AuthorizedException("登录状态已失效，请重新登录");
        }
        return (Integer) userId;
    }
}
