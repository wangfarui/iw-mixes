package com.itwray.iw.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.RedisKeyConstants;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.core.SpringWebHolder;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.itwray.iw.web.utils.UserUtils.TOKEN_HEADER;

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
        // 查询用户是否存在
        AuthUserEntity authUserEntity;
        if (NumberUtils.isValidPhoneNumber(dto.getAccount())) {
            authUserEntity = authUserDao.queryOneByPhoneNumber(dto.getAccount());
        } else {
            authUserEntity = authUserDao.queryOneByUsername(dto.getAccount());
        }

        // 用户不存在，抛出账号验证异常信息
        if (authUserEntity == null) {
            throw this.accountVerifyException();
        }

        // 校验密码正确性, 通过后表示登录成功
        this.verifyPassword(dto.getPassword(), authUserEntity.getPassword());

        // TODO 后期改定时执行 先清除历史过期token set
        Set<String> userTokens = RedisUtil.members(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId(), String.class);
        if (userTokens != null) {
            for (String token : userTokens) {
                // 如果token已过期，则删除set集合中的value
                if (!RedisUtil.hasKey(RedisKeyConstants.USER_TOKEN_KEY + token)) {
                    RedisUtil.remove(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId(), token);
                }
            }
        }

        // 生成Token并缓存
        String token = UUID.randomUUID().toString();
        RedisUtil.set(RedisKeyConstants.USER_TOKEN_KEY + token, authUserEntity.getId(), ACTIVE_TIME);
        RedisUtil.sSet(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId(), token);
        RedisUtil.expire(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId(), ACTIVE_TIME);

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
        // 获取当前请求token
        String token = UserUtils.getToken();
        if (token == null) {
            return;
        }

        // 获取当前token的用户id
        Integer userId = this.getUserId(token);

        // 移除token缓存
        RedisUtil.delete(RedisKeyConstants.USER_TOKEN_KEY + token);
        RedisUtil.remove(RedisKeyConstants.USER_TOKEN_SET_KEY + userId, token);
    }

    /**
     * 校验token有效性(默认续签)
     *
     * @param token token值
     * @return true -> 有效
     */
    @Override
    public Boolean validateToken(String token) {
        return this.validateToken(token, true);
    }

    /**
     * 获取指定token的用户id
     *
     * @param token token值
     * @return 用户id
     */
    @Override
    public Integer getUserId(String token) {
        return getLoginId(token);
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
        if (!RedisUtil.hasKey(RedisKeyConstants.USER_TOKEN_KEY + token)) {
            return false;
        }

        // 自动续签
        if (isRenew) {
            RedisUtil.expire(RedisKeyConstants.USER_TOKEN_KEY + token, ACTIVE_TIME);
        }

        return true;
    }

    @Override
    @Transactional
    public void editAvatar(String avatar) {
        AuthUserEntity authUserEntity = getCurrentUser();
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, authUserEntity.getId())
                .set(AuthUserEntity::getAvatar, avatar)
                .update();
    }

    @Override
    @Transactional
    public void editPassword(UserPasswordEditDto dto) {
        AuthUserEntity authUserEntity = getCurrentUser();
        this.verifyPassword(dto.getOldPassword(), authUserEntity.getPassword());

        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, authUserEntity.getId())
                .set(AuthUserEntity::getPassword, BCrypt.hashpw((dto.getNewPassword())))
                .update();

        // 密码修改成功之后，清除历史token缓存
        Set<String> userTokens = RedisUtil.members(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId(), String.class);
        if (userTokens != null) {
            for (String token : userTokens) {
                RedisUtil.delete(RedisKeyConstants.USER_TOKEN_KEY + token);
            }
        }
        RedisUtil.delete(RedisKeyConstants.USER_TOKEN_SET_KEY + authUserEntity.getId());
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
        return this.getLoginId(token);
    }

    /**
     * 获取指定token的用户id
     */
    private Integer getLoginId(String token) {
        Boolean validity = this.validateToken(token, false);
        if (!validity) {
            throw new AuthorizedException("登录状态已失效，请重新登录");
        }
        Object userId = RedisUtil.get(RedisKeyConstants.USER_TOKEN_KEY + token);
        if (userId == null) {
            throw new AuthorizedException("登录状态已失效，请重新登录");
        }
        return (Integer) userId;
    }

    /**
     * 获取当前登录用户
     *
     * @return AuthUserEntity
     */
    private AuthUserEntity getCurrentUser() {
        Integer loginId = this.getLoginId();
        AuthUserEntity userEntity = authUserDao.getById(loginId);
        if (userEntity == null) {
            throw new AuthorizedException("用户不存在，请重新登录");
        }
        return userEntity;
    }

    /**
     * 校验密码是否一致
     *
     * @param originalPassword 原始密码
     * @param encryptPassword  加密密码
     */
    private void verifyPassword(String originalPassword, String encryptPassword) {
        if (!BCrypt.checkpw(originalPassword, encryptPassword)) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw this.accountVerifyException();
        }
    }

    /**
     * 账号验证异常
     *
     * @return 账号或密码错误
     */
    private BusinessException accountVerifyException() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new BusinessException("账号密码错误");
    }

}
