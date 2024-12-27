package com.itwray.iw.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.LoginVerificationCodeDto;
import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.enums.VerificationCodeActionEnum;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.utils.SpringWebHolder;
import com.itwray.iw.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.itwray.iw.common.constants.RequestHeaderConstants.TOKEN_HEADER;

/**
 * 用户服务实现层
 *
 * @author wray
 * @since 2024/3/2
 */
@Service
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserDao authUserDao;

    private AuthRegisterService authRegisterService;

    @Autowired
    public AuthUserServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Autowired
    public void setAuthRegisterService(AuthRegisterService authRegisterService) {
        this.authRegisterService = authRegisterService;
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

        return authUserDao.loginSuccessAfter(authUserEntity);
    }

    @Override
    public UserInfoVo loginByVerificationCode(LoginVerificationCodeDto dto) {
        // 校验电话号码是否正确
        if (!NumberUtils.isValidPhoneNumber(dto.getPhoneNumber())) {
            throw this.accountVerifyException("电话号码格式错误");
        }

        // 校验电话号码验证码的正确性, 通过后表示登录成功
        String verificationCode = RedisUtil.get(AuthRedisKeyEnum.PHONE_VERIFY_KEY.getKey(dto.getPhoneNumber()), String.class);
        if (verificationCode == null || !verificationCode.equals(dto.getVerificationCode())) {
            throw this.accountVerifyException("验证码错误");
        }

        // 根据电话号码查询用户
        AuthUserEntity authUserEntity = authUserDao.queryOneByPhoneNumber(dto.getPhoneNumber());
        // 如果用户不存在，则在验证码校验通过的前提下，自动注册新用户
        if (authUserEntity == null) {
            UserAddBo userAddBo = new UserAddBo(dto.getPhoneNumber());
            userAddBo.setPassword(dto.getPassword());
            authUserEntity = authUserDao.addNewUser(userAddBo);
        }

        return authUserDao.loginSuccessAfter(authUserEntity);
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
        RedisUtil.delete(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token));
        RedisUtil.remove(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(userId), token);
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
        if (!RedisUtil.hasKey(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token))) {
            return false;
        }

        // 自动续签
        if (isRenew) {
            Object userId = RedisUtil.get(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token));
            if (userId != null) {
                RedisUtil.expire(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(userId), AuthUserDao.TOKEN_ACTIVE_TIME);
                RedisUtil.expire(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token), AuthUserDao.TOKEN_ACTIVE_TIME);
            }
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
        // 获取当前用户实体
        AuthUserEntity authUserEntity = getCurrentUser();

        // 验证码不为空的情况下, 优先使用验证码校验
        if (StringUtils.isNotBlank(dto.getVerificationCode())) {
            // 校验电话号码验证码的正确性, 通过后表示登录成功
            String verificationCode = RedisUtil.get(AuthRedisKeyEnum.PHONE_VERIFY_KEY.getKey(authUserEntity.getPhoneNumber()), String.class);
            if (verificationCode == null || !verificationCode.equals(dto.getVerificationCode())) {
                throw this.accountVerifyException("验证码错误");
            }
        } else if (StringUtils.isNotBlank(dto.getOldPassword())) {
            this.verifyPassword(dto.getOldPassword(), authUserEntity.getPassword());
        } else {
            throw new BusinessException("无法识别的操作");
        }

        // 验证成功后, 修改密码
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, authUserEntity.getId())
                .set(AuthUserEntity::getPassword, BCrypt.hashpw((dto.getNewPassword())))
                .update();

        // 密码修改成功之后，清除历史token缓存
        Set<String> userTokens = RedisUtil.members(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(authUserEntity.getId()), String.class);
        if (userTokens != null) {
            for (String token : userTokens) {
                RedisUtil.delete(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token));
            }
        }
        RedisUtil.delete(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(authUserEntity.getId()));
    }

    @Override
    public void getVerificationCodeByAction(Integer action, String clientIp) {
        VerificationCodeActionEnum actionEnum = VerificationCodeActionEnum.of(action);
        String phoneNumber = null;
        switch (actionEnum) {
            case EDIT_PASSWORD -> {
                Integer userId = UserUtils.getUserId();
                AuthUserEntity authUserEntity = authUserDao.getById(userId);
                if (authUserEntity == null) {
                    throw new BusinessException("用户不存在，请刷新重试");
                }
                phoneNumber = authUserEntity.getPhoneNumber();
            }
            case OTHER -> log.info("忽略其他操作");
        }
        // TODO 暂时直接使用注册服务获取短信验证码的机制
        if (phoneNumber != null) {
            authRegisterService.getVerificationCode(phoneNumber, clientIp);
        }
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
        Object userId = RedisUtil.get(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token));
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
    private BusinessException accountVerifyException(String... exceptionMessage) {
        // TODO 针对任何登录方式，加上登录失败的重试次数
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new BusinessException(exceptionMessage != null && exceptionMessage.length > 0 ? exceptionMessage[0] : "账号密码错误");
    }

}
