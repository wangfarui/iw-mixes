package com.itwray.iw.bookkeeping.core.mybatis;

import com.itwray.iw.auth.client.AuthFamilyGroupClient;
import com.itwray.iw.auth.model.vo.FamilySharedQueryPolicyVo;
import com.itwray.iw.web.core.mybatis.UserCurrentGroupProvider;
import com.itwray.iw.web.core.mybatis.UserSharedQueryPolicy;
import com.itwray.iw.common.constants.BoolEnum;
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

    @Override
    public UserSharedQueryPolicy querySharedQueryPolicy(Integer userId) {
        try {
            FamilySharedQueryPolicyVo policyVo = authFamilyGroupClient.querySharedQueryPolicy(userId);
            if (policyVo == null) {
                return new UserSharedQueryPolicy(0, false);
            }
            Integer currentGroupId = policyVo.getCurrentGroupId() == null ? 0 : policyVo.getCurrentGroupId();
            boolean forceQueryOnlyMyself = BoolEnum.TRUE.getCode().equals(policyVo.getForceQueryOnlyMyself());
            return new UserSharedQueryPolicy(currentGroupId, forceQueryOnlyMyself);
        } catch (Exception e) {
            log.error("查询用户共享查询策略失败, userId: {}", userId, e);
            return new UserSharedQueryPolicy(0, false);
        }
    }
}
