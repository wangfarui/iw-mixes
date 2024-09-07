package com.itwray.iw.web.utils;

import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.core.SpringWebHolder;
import com.itwray.iw.web.exception.AuthorizedException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户工具类
 *
 * @author wray
 * @since 2024/9/6
 */
public abstract class UserUtils {

    /**
     * token的固定header
     */
    public static final String TOKEN_HEADER = "iwtoken";

    /**
     * 获取当前登录用户的id
     * <p>TODO 在初次获取后，可以放到线程域中缓存，等线程结束时再清除</p>
     */
    public static Integer getUserId() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        String token = request.getHeader(TOKEN_HEADER);
        if (token == null) {
            throw new AuthorizedException("当前未登录，请先登录");
        }

        Object userId = RedisUtil.get(token);
        if (userId == null) {
            throw new AuthorizedException("登录状态已失效，请重新登录");
        }
        return (Integer) userId;
    }

}
