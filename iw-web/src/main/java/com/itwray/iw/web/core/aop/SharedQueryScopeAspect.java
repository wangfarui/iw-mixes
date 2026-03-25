package com.itwray.iw.web.core.aop;

import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.web.core.mybatis.UserCurrentGroupProvider;
import com.itwray.iw.web.core.mybatis.UserSharedQueryPolicy;
import com.itwray.iw.web.model.dto.SharedQueryRequest;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import com.itwray.iw.web.utils.UserCurrentGroupUtils;
import com.itwray.iw.web.utils.UserSharedQueryUtils;
import com.itwray.iw.web.utils.UserUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 共享查询作用域切面
 *
 * @author wray
 * @since 2026/3/18
 */
@Aspect
public class SharedQueryScopeAspect {

    private static final Object USER_CURRENT_GROUP_PROVIDER_LOCK = new Object();

    private static volatile UserCurrentGroupProvider userCurrentGroupProvider;

    private static volatile boolean userCurrentGroupProviderResolved = false;

    @Around("@within(com.itwray.iw.web.annotation.SharedQueryScope) || @annotation(com.itwray.iw.web.annotation.SharedQueryScope)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            UserSharedQueryPolicy policy = this.querySharedQueryPolicy();
            if (policy != null && policy.getCurrentGroupId() != null) {
                UserCurrentGroupUtils.setCurrentGroupId(policy.getCurrentGroupId());
            }
            UserSharedQueryUtils.setUserSharedQuery(true);
            boolean requestOnlyMyself = BoolEnum.TRUE.getCode().equals(this.resolveQueryOnlyMyself(joinPoint.getArgs()));
            boolean forceOnlyMyself = policy != null && policy.isForceQueryOnlyMyself();
            UserSharedQueryUtils.setUserSharedQueryOnlyMyself(requestOnlyMyself || forceOnlyMyself);
            return joinPoint.proceed();
        } finally {
            UserSharedQueryUtils.removeUserSharedQuery();
            UserSharedQueryUtils.removeUserSharedQueryOnlyMyself();
            UserCurrentGroupUtils.removeCurrentGroupId();
        }
    }

    private Integer resolveQueryOnlyMyself(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof SharedQueryRequest) {
                SharedQueryRequest request = (SharedQueryRequest) arg;
                return request.getQueryOnlyMyself();
            }
        }
        return null;
    }

    private UserSharedQueryPolicy querySharedQueryPolicy() {
        Integer userId = UserUtils.getUserId(false);
        if (userId == null) {
            return null;
        }
        UserCurrentGroupProvider provider = getUserCurrentGroupProvider();
        if (provider == null) {
            return null;
        }
        try {
            return provider.querySharedQueryPolicy(userId);
        } catch (Exception e) {
            return null;
        }
    }

    private UserCurrentGroupProvider getUserCurrentGroupProvider() {
        if (userCurrentGroupProviderResolved) {
            return userCurrentGroupProvider;
        }
        synchronized (USER_CURRENT_GROUP_PROVIDER_LOCK) {
            if (userCurrentGroupProviderResolved) {
                return userCurrentGroupProvider;
            }
            if (!ApplicationContextHolder.hasApplicationContext()) {
                return null;
            }
            userCurrentGroupProvider = ApplicationContextHolder.getBeanProvider(UserCurrentGroupProvider.class)
                    .getIfAvailable();
            userCurrentGroupProviderResolved = true;
            return userCurrentGroupProvider;
        }
    }
}
