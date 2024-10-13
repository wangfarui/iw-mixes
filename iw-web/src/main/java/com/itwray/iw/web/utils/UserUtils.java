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
     * <ul>常用于如下地方：
     *     <li>用户数据权限</li>
     *     <li>mybatis-plus默认数据填充</li>
     *     <li>业务层手动引用</li>
     * </ul>
     */
    public static Integer getUserId() {
        Integer userId = USER_ID.get();
        // 线程中为空时，尝试远程获取
        if (userId == null) {
            String token = getToken();
            if (token == null) {
                throw new AuthorizedException("当前未登录，请先登录");
            }

            setUserId(userId = ClientHelper.getAuthClient().getUserIdByToken(token));
        }
        return userId;
    }

    /**
     * 设置当前用户id
     * <p>当线程上下文不存在token时，可手动赋值</p>
     *
     * @param userId 用户id
     */
    public static void setUserId(Integer userId) {
        USER_ID.set(userId);
    }

    /**
     * 清除线程的用户id
     */
    public static void removeUserId() {
        USER_ID.remove();
    }

}
