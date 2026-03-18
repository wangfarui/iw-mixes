package com.itwray.iw.web.utils;

/**
 * 用户当前家庭组上下文工具
 *
 * @author wray
 * @since 2026/3/12
 */
public abstract class UserCurrentGroupUtils {

    /**
     * 当前线程缓存的家庭组ID
     * <p>默认为 null 表示未初始化</p>
     */
    private static final ThreadLocal<Integer> USER_CURRENT_GROUP_ID = new ThreadLocal<>();

    /**
     * 获取当前线程缓存的家庭组ID
     */
    public static Integer getCurrentGroupId() {
        return USER_CURRENT_GROUP_ID.get();
    }

    /**
     * 设置当前线程缓存的家庭组ID
     */
    public static void setCurrentGroupId(Integer groupId) {
        USER_CURRENT_GROUP_ID.set(groupId);
    }

    /**
     * 清除当前线程缓存的家庭组ID
     */
    public static void removeCurrentGroupId() {
        USER_CURRENT_GROUP_ID.remove();
    }
}
