package com.itwray.iw.web.utils;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.web.client.ClientHelper;
import com.itwray.iw.web.core.SpringWebHolder;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.IwWebException;
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

    /**
     * 获取当前登录用户的id
     */
    public static Integer getUserId() {
        Integer userId = USER_ID.get();
        // 线程中为空时，尝试远程获取
        if (userId == null) {
            HttpServletRequest request = SpringWebHolder.getRequest();
            String token = request.getHeader(TOKEN_HEADER);
            if (token == null) {
                throw new AuthorizedException("当前未登录，请先登录");
            }

            GeneralResponse<Integer> response = ClientHelper.getAuthClient().getUserIdByToken(token);
            if (!response.isSuccess()) {
                throw new IwWebException("用户信息异常，请重试");
            }
            userId = response.getData();
            USER_ID.set(userId);
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
