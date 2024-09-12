package com.itwray.iw.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.auth.core.AuthServiceException;
import com.itwray.iw.auth.mapper.AuthUserMapper;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.web.constants.WebCommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 用户 Dao
 *
 * @author wray
 * @since 2024/3/2
 */
@Component
public class AuthUserDao extends ServiceImpl<AuthUserMapper, AuthUserEntity> {

    /**
     * 根据用户名查询唯一的用户
     *
     * @param username 用户名
     * @return 可能为空的用户
     */
    public @Nullable AuthUserEntity queryOneByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new AuthServiceException("用户名不能为空");
        }
        return this.lambdaQuery()
                .eq(AuthUserEntity::getUsername, username)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
    }

    /**
     * 根据用户名修改用户密码
     *
     * @param username 用户名
     * @param encodedPassword 加密过后的密码
     * @return 是否修改成功
     */
    public boolean updatePasswordByUsername(String username, String encodedPassword) {
        return this.lambdaUpdate()
                .eq(AuthUserEntity::getUsername, username)
                .set(AuthUserEntity::getPassword, encodedPassword)
                .update();
    }
}
