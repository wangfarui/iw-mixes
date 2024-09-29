package com.itwray.iw.web.utils;

import com.itwray.iw.web.client.ClientHelper;
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

    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();

    public static String getToken() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        return request.getHeader(TOKEN_HEADER);
    }

    /**
     * 获取当前登录用户的id
     */
    public static Integer getUserId() {
        Integer userId = USER_ID.get();
        // 线程中为空时，尝试远程获取
        if (userId == null) {
            String token = getToken();
            if (token == null) {
                throw new AuthorizedException("当前未登录，请先登录");
            }

            USER_ID.set(userId = ClientHelper.getAuthClient().getUserIdByToken(token));
        }
        return userId;
    }

    /**
     * 清除线程的用户id
     */
    public static void removeUserId() {
        USER_ID.remove();
    }

}
