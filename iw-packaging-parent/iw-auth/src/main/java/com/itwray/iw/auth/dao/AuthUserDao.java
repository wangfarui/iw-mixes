package com.itwray.iw.auth.dao;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.auth.mapper.AuthUserMapper;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.enums.mq.RegisterNewUserTopicEnum;
import com.itwray.iw.web.utils.IpUtils;
import com.itwray.iw.web.utils.SpringWebHolder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.itwray.iw.common.constants.RequestHeaderConstants.TOKEN_HEADER;

/**
 * 用户 Dao
 *
 * @author wray
 * @since 2024/3/2
 */
@Component
public class AuthUserDao extends BaseDao<AuthUserMapper, AuthUserEntity> {

    /**
     * 新增用户
     *
     * @param bo 用户新增对象
     * @return 用户实体
     */
    @Transactional
    public AuthUserEntity addNewUser(UserAddBo bo) {
        if (StringUtils.isBlank(bo.getPhoneNumber())) {
            throw new IwWebException("电话号码不能为空");
        }

        // 校验用户唯一性
        this.checkUserUnique(bo.getPhoneNumber(), bo.getUsername());

        // 如果密码为空，则生成随机密码
        if (StringUtils.isBlank(bo.getPassword())) {
            bo.setPassword(RandomUtil.randomString(64));
        }
        // 填充用户名和姓名
        if (StringUtils.isBlank(bo.getUsername())) {
            bo.setUsername(bo.getPhoneNumber());
        }
        if (StringUtils.isBlank(bo.getName())) {
            bo.setName(bo.getUsername());
        }

        // 保存用户
        AuthUserEntity addUser = new AuthUserEntity();
        BeanUtils.copyProperties(bo, addUser);
        // 密码基于 BCrypt 加密存储
        addUser.setPassword(BCrypt.hashpw((addUser.getPassword())));
        this.save(addUser);

        // 发送注册新用户成功的MQ消息
        bo.setUserId(addUser.getId());
        MQProducerHelper.send(RegisterNewUserTopicEnum.INIT, bo);

        return addUser;
    }

    /**
     * 登录操作前的动作
     *
     * @param account 登录账号
     */
    public void loginBefore(String account) {
        String clientIp = IpUtils.getCurrentClientIp();

        // 判断当前客户端ip短时间内的登录失败次数是否超过上限
        Integer ipFailCount = RedisUtil.get(AuthRedisKeyEnum.LOGIN_FAIL_IP_KEY.getKey(clientIp), Integer.class);
        if (ipFailCount != null && ipFailCount >= 10) {
            throw new BusinessException("操作频繁，请稍后再试");
        }

        // 判断当前用户和客户端ip短时间内的登录失败次数是否超过上限
        Integer userIpFailCount = RedisUtil.get(AuthRedisKeyEnum.LOGIN_ACTION_USER_IP_KEY.getKey(account, clientIp), Integer.class);
        if (userIpFailCount != null && userIpFailCount >= 5) {
            throw new BusinessException("操作频繁，请稍后再试");
        }
    }

    /**
     * 登录成功之后的操作
     *
     * @param authUserEntity 用户实体
     * @return 用户登录信息
     */
    public UserInfoVo loginSuccessAfter(AuthUserEntity authUserEntity) {
        // 更新用户最后登录时间
        this.lambdaUpdate()
                .eq(AuthUserEntity::getId, authUserEntity.getId())
                .set(AuthUserEntity::getLastLoginTime, LocalDateTime.now())
                .update();

        // 生成Token并缓存
        String token = UUID.randomUUID().toString().replace("-", "");
        AuthRedisKeyEnum.USER_TOKEN_KEY.setStringValue(authUserEntity.getId(), token);
        RedisUtil.sSet(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(authUserEntity.getId()), token);
        AuthRedisKeyEnum.USER_TOKEN_SET_KEY.setExpire(authUserEntity.getId());

        // 将token写入到请求头中
        this.setTokenValue(token);

        // 构建响应对象
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setName(authUserEntity.getName());
        if (authUserEntity.getAvatar() != null) {
            userInfoVo.setAvatar(authUserEntity.getAvatar());
        }
        userInfoVo.setTokenName(TOKEN_HEADER);
        userInfoVo.setTokenValue(token);

        return userInfoVo;
    }

    /**
     * 根据用户名查询唯一的用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    public @Nullable AuthUserEntity queryOneByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BusinessException("用户名不能为空");
        }
        return this.lambdaQuery()
                .eq(AuthUserEntity::getUsername, username)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
    }

    /**
     * 根据电话号码查询唯一的用户
     *
     * @param phoneNumber 电话号码
     * @return 用户实体
     */
    public @Nullable AuthUserEntity queryOneByPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BusinessException("电话号码不能为空");
        }
        return this.lambdaQuery()
                .eq(AuthUserEntity::getPhoneNumber, phoneNumber)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
    }

    /**
     * 根据用户名修改用户密码
     *
     * @param username        用户名
     * @param encodedPassword 加密过后的密码
     * @return 是否修改成功
     */
    public boolean updatePasswordByUsername(String username, String encodedPassword) {
        return this.lambdaUpdate()
                .eq(AuthUserEntity::getUsername, username)
                .set(AuthUserEntity::getPassword, encodedPassword)
                .update();
    }

    /**
     * 自增客户端锁次数
     *
     * @param clientIp 客户端ip
     */
    public void incrementClientIpLockCount(String clientIp) {
        // 不限制内部客户端ip
        if (WebCommonConstants.INNER_CLIENT_IP.equals(clientIp)) {
            return;
        }
        RedisUtil.incrementOne(AuthRedisKeyEnum.REGISTER_IP_KEY.getKey(clientIp));
        AuthRedisKeyEnum.REGISTER_IP_KEY.setExpire(clientIp);
    }

    /**
     * 校验用户是否唯一
     *
     * @param phoneNumber 唯一电话号码
     * @param username    唯一用户名
     */
    public void checkUserUnique(@Nullable String phoneNumber, @Nullable String username) {
        // 获取客户端ip
        String clientIp;
        if (SpringWebHolder.isWeb()) {
            clientIp = IpUtils.getClientIp(SpringWebHolder.getRequest());
        } else {
            // 非web请求，则为内部客户端ip
            clientIp = WebCommonConstants.INNER_CLIENT_IP;
        }

        // 校验电话号码是否已注册
        if (StringUtils.isNotBlank(phoneNumber)) {
            AuthUserEntity authUserEntity = this.queryOneByPhoneNumber(phoneNumber);
            if (authUserEntity != null) {
                // 为防止用户恶意猜测电话号码，增加注册次数限制
                this.incrementClientIpLockCount(clientIp);
                throw new BusinessException("用户已存在");
            }
        }

        // 校验用户名是否已注册
        if (StringUtils.isNotBlank(username)) {
            AuthUserEntity authUserEntity = this.queryOneByUsername(username);
            if (authUserEntity != null) {
                // 为防止用户恶意猜测用户名，增加注册次数限制
                this.incrementClientIpLockCount(clientIp);
                throw new BusinessException("用户已存在");
            }
        }
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
}
