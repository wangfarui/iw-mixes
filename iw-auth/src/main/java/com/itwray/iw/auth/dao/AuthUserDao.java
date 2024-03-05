package com.itwray.iw.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.auth.entity.AuthUser;
import com.itwray.iw.auth.mapper.AuthUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 用户 DAO
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Component
public class AuthUserDao extends ServiceImpl<AuthUserMapper, AuthUser> {

    public @Nullable AuthUser queryOneByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            throw new RuntimeException("账号不能为空");
        }
        return this.lambdaQuery()
                .eq(AuthUser::getAccount, account)
                .last("limit 1")
                .one();
    }
}
