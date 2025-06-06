package com.itwray.iw.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.LoginVerificationCodeDto;
import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.model.dto.UserUsernameEditDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.enums.VerificationCodeActionEnum;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.auth.service.AuthVerificationService;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisKeyManager;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.redis.lock.DistributedLock;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.utils.IpUtils;
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

    private AuthVerificationService authVerificationService;

    @Autowired
    public AuthUserServiceImpl(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Autowired
    public void setAuthVerificationService(AuthVerificationService authVerificationService) {
        this.authVerificationService = authVerificationService;
    }

    /**
     * 通过自定义Web接口访问
     *
     * @param dto 登录密码信息
     * @return 用户登录信息
     */
    @Override
    public UserInfoVo loginByPassword(LoginPasswordDto dto) {
        authUserDao.loginBefore(dto.getAccount());

        // 查询用户是否存在
        AuthUserEntity authUserEntity = authUserDao.queryOneByUsername(dto.getAccount());
        if (authUserEntity == null) {
            if (NumberUtils.isValidPhoneNumber(dto.getAccount())) {
                authUserEntity = authUserDao.queryOneByPhoneNumber(dto.getAccount());
            } else if (NumberUtils.isValidEmailAddress(dto.getAccount())) {
                authUserEntity = authUserDao.queryOneByEmailAddress(dto.getAccount());
            }
        }

        // 用户不存在，抛出账号验证异常信息
        if (authUserEntity == null) {
            throw this.accountVerifyException(dto.getAccount(), "账号密码错误");
        }

        // 校验密码正确性, 通过后表示登录成功
        this.verifyPassword(dto.getAccount(), dto.getPassword(), authUserEntity.getPassword());

        return authUserDao.loginSuccessAfter(authUserEntity);
    }

    @Override
    public UserInfoVo loginByVerificationCode(LoginVerificationCodeDto dto) {
        // 用户登录校验使用的账号
        String account;
        // 用户登录校验使用的Redis Key
        RedisKeyManager redisKeyManager;
        switch (dto.getLoginWay()) {
            case PHONE -> {
                account = dto.getPhoneNumber();
                // 校验电话号码是否正确
                if (!NumberUtils.isValidPhoneNumber(account)) {
                    throw this.accountVerifyException(account, "电话号码格式错误");
                }
                redisKeyManager = AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY;
            }
            case EMAIL -> {
                account = dto.getEmailAddress();
                // 校验邮箱是否正确
                if (!NumberUtils.isValidEmailAddress(account)) {
                    throw this.accountVerifyException(account, "邮箱格式错误");
                }
                redisKeyManager = AuthRedisKeyEnum.USER_LOGIN_EMAIL_VERIFY_KEY;
            }
            default -> throw new BusinessException("不支持的业务操作");
        }

        authUserDao.loginBefore(account);

        // 校验电话号码验证码的正确性, 通过后表示登录成功
        boolean compareResult = authVerificationService.compareVerificationCode(dto.getVerificationCode(), account, redisKeyManager);
        if (!compareResult) {
            throw this.accountVerifyException(account, "验证码错误");
        }

        AuthUserEntity authUserEntity = authUserDao.queryOneByLoginWay(account, dto.getLoginWay());
        // 如果用户不存在，则在验证码校验通过的前提下，自动注册新用户
        if (authUserEntity == null) {
            UserAddBo userAddBo = new UserAddBo();
            userAddBo.setPhoneNumber(dto.getPhoneNumber());
            userAddBo.setEmailAddress(dto.getEmailAddress());
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
                AuthRedisKeyEnum.USER_TOKEN_SET_KEY.setExpire(userId);
                AuthRedisKeyEnum.USER_TOKEN_KEY.setExpire(token);
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

        // 手机验证码不为空的情况下, 优先使用手机验证码校验
        if (StringUtils.isNotBlank(dto.getVerificationCode())) {
            // 校验电话号码验证码的正确性
            String verificationCode = RedisUtil.get(AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY.getKey(authUserEntity.getPhoneNumber()), String.class);
            if (verificationCode == null || !verificationCode.equals(dto.getVerificationCode())) {
                throw this.accountVerifyException(authUserEntity.getUsername(), "验证码错误");
            }
        }
        // 邮箱验证码不为空的情况下, 使用邮箱验证码校验
        else if (StringUtils.isNotBlank(dto.getEmailVerificationCode())) {
            // 校验邮箱验证码的正确性
            String verificationCode = RedisUtil.get(AuthRedisKeyEnum.USER_LOGIN_EMAIL_VERIFY_KEY.getKey(authUserEntity.getEmailAddress()), String.class);
            if (verificationCode == null || !verificationCode.equals(dto.getEmailVerificationCode())) {
                throw this.accountVerifyException(authUserEntity.getUsername(), "验证码错误");
            }
        }
        // 使用原密码校验
        else if (StringUtils.isNotBlank(dto.getOldPassword())) {
            this.verifyPassword(authUserEntity.getUsername(), dto.getOldPassword(), authUserEntity.getPassword());
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
    public void getVerificationCodeByAction(Integer action) {
        VerificationCodeActionEnum actionEnum = ConstantEnumUtil.findByType(VerificationCodeActionEnum.class, action);
        Integer userId = UserUtils.getUserId();
        AuthUserEntity authUserEntity = authUserDao.getById(userId);
        if (authUserEntity == null) {
            throw new BusinessException("用户不存在，请刷新重试");
        }
        switch (actionEnum) {
            case PHONE_EDIT_PASSWORD, APPLICATION_ACCOUNT_REFRESH_PASSWORD -> {
                if (StringUtils.isBlank(authUserEntity.getPhoneNumber())) {
                    throw new BusinessException("未绑定手机号");
                }
                authVerificationService.getPhoneVerificationCode(authUserEntity.getPhoneNumber(), actionEnum.getKeyManager());
            }
            case EMAIL_EDIT_PASSWORD -> {
                if (StringUtils.isBlank(authUserEntity.getEmailAddress())) {
                    throw new BusinessException("未绑定邮箱");
                }
                authVerificationService.getEmailVerificationCode(authUserEntity.getEmailAddress(), actionEnum.getKeyManager());
            }
            default -> throw new BusinessException("不支持的操作");
        }
    }

    @Override
    @Transactional
    @DistributedLock(lockName = "'user:save:unique:key'") // 在修改保存用户的唯一值的操作上,必须加上分布式锁,以确保不出现重复数据
    public void editUsername(UserUsernameEditDto dto) {
        Integer userId = UserUtils.getUserId();
        AuthUserEntity authUserEntity = authUserDao.queryById(userId);
        // 用户名没变的情况下,直接忽略请求
        if (dto.getUsername().equals(authUserEntity.getUsername())) {
            return;
        }

        // 校验用户名是否重复
        authUserDao.checkUserUnique(null, dto.getUsername(), null);

        // 更新用户名
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, userId)
                // 校验用户名, 避免并发
                .eq(AuthUserEntity::getUsername, authUserEntity.getUsername())
                .set(AuthUserEntity::getUsername, dto.getUsername())
                .update();
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
     * @param account          用户账号
     * @param originalPassword 原始密码
     * @param encryptPassword  加密密码
     */
    private void verifyPassword(String account, String originalPassword, String encryptPassword) {
        if (!BCrypt.checkpw(originalPassword, encryptPassword)) {
            throw this.accountVerifyException(account, "账号密码错误");
        }
    }

    /**
     * 账号验证异常
     *
     * @param account          用户账号
     * @param exceptionMessage 异常信息
     * @return 账号或密码错误
     */
    private BusinessException accountVerifyException(String account, String exceptionMessage) {
        String clientIp = IpUtils.getCurrentClientIp();
        // 同一ip, 5分钟内增加失败次数
        RedisUtil.incrementOne(AuthRedisKeyEnum.LOGIN_FAIL_IP_KEY.getKey(clientIp));
        AuthRedisKeyEnum.LOGIN_FAIL_IP_KEY.setExpire(clientIp);
        // 同一用户和ip, 5分钟内增加失败次数
        RedisUtil.incrementOne(AuthRedisKeyEnum.LOGIN_ACTION_USER_IP_KEY.getKey(account, clientIp));
        AuthRedisKeyEnum.LOGIN_ACTION_USER_IP_KEY.setExpire(account, clientIp);

        log.info("用户登录失败，账号：{}, 失败原因：{}", account, exceptionMessage);

        // 凡是账号验证失败的情况，通过延迟1s，避免用户根据异常信息恶意揣测合法用户的信息
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new BusinessException(exceptionMessage);
    }

}
