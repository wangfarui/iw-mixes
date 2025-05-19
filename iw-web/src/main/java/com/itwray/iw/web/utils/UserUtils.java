package com.itwray.iw.web.utils;

import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.web.client.AuthenticationClient;
import com.itwray.iw.web.exception.AuthorizedException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

/**
 * 用户工具类
 *
 * @author wray
 * @since 2024/9/6
 */
public abstract class UserUtils {

    /**
     * 当前线程的用户id
     */
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();

    /**
     * 当前线程是否开启用户数据权限
     * <p>默认为 null 时表示开启</p>
     */
    private static final ThreadLocal<Boolean> USER_DATA_PERMISSION = new ThreadLocal<>();

    private static volatile AuthenticationClient authenticationClient;

    /**
     * authenticationClient 对象锁
     */
    private static final Object AUTHENTICATION_CLIENT_LOCK = new Object();

    public static @Nonnull String getToken() {
        return getToken(true);
    }

    public static @Nullable String getToken(boolean required) {
        try {
            HttpServletRequest request = SpringWebHolder.getRequest();
            return request.getHeader(RequestHeaderConstants.TOKEN_HEADER);
        } catch (IllegalStateException e) {
            // ignore
        }
        return null;
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

            setUserId(userId = getAuthClient().getUserIdByToken(token));
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

    /**
     * 获取当前线程是否开启数据权限
     */
    public static boolean getUserDataPermission() {
        Boolean bool = USER_DATA_PERMISSION.get();
        return bool == null || bool;
    }

    /**
     * 设置用户数据权限
     */
    public static void setUserDataPermission(Boolean dataPermission) {
        USER_DATA_PERMISSION.set(dataPermission);
    }

    /**
     * 清除线程的用户数据权限
     */
    public static void removeUserDataPermission() {
        USER_DATA_PERMISSION.remove();
    }

    private static AuthenticationClient getAuthClient() {
        if (authenticationClient == null) {
            synchronized (AUTHENTICATION_CLIENT_LOCK) {
                if (authenticationClient == null) {
                    authenticationClient = ApplicationContextHolder.getBean(AuthenticationClient.class);
                }
            }
        }
        return authenticationClient;
    }
}
