package com.itwray.iw.web.core.aop;

import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.web.model.dto.SharedQueryRequest;
import com.itwray.iw.web.utils.UserCurrentGroupUtils;
import com.itwray.iw.web.utils.UserSharedQueryUtils;
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

    @Around("@within(com.itwray.iw.web.annotation.SharedQueryScope) || @annotation(com.itwray.iw.web.annotation.SharedQueryScope)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            UserSharedQueryUtils.setUserSharedQuery(true);
            UserSharedQueryUtils.setUserSharedQueryOnlyMyself(BoolEnum.TRUE.getCode().equals(this.resolveQueryOnlyMyself(joinPoint.getArgs())));
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
            if (arg instanceof SharedQueryRequest request) {
                return request.getQueryOnlyMyself();
            }
        }
        return null;
    }
}
