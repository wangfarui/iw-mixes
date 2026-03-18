package com.itwray.iw.bookkeeping.core.mybatis;

import com.itwray.iw.auth.client.AuthFamilyGroupClient;
import com.itwray.iw.web.core.mybatis.UserCurrentGroupProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记账服务当前家庭组提供器
 *
 * @author wray
 * @since 2026/3/12
 */
@Component
@Slf4j
public class BookkeepingCurrentGroupProvider implements UserCurrentGroupProvider {

    private final AuthFamilyGroupClient authFamilyGroupClient;

    @Autowired
    public BookkeepingCurrentGroupProvider(AuthFamilyGroupClient authFamilyGroupClient) {
        this.authFamilyGroupClient = authFamilyGroupClient;
    }

    @Override
    public Integer queryCurrentGroupId(Integer userId) {
        try {
            Integer groupId = authFamilyGroupClient.queryCurrentGroupId(userId);
            return groupId == null ? 0 : groupId;
        } catch (Exception e) {
            log.error("查询用户当前家庭组ID失败, userId: {}", userId, e);
            return 0;
        }
    }
}
