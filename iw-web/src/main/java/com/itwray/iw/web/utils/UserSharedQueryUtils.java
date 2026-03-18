package com.itwray.iw.web.utils;

/**
 * 用户共享查询上下文工具
 *
 * @author wray
 * @since 2026/3/12
 */
public abstract class UserSharedQueryUtils {

    /**
     * 当前线程是否开启共享查询
     * <p>默认为 null 时表示关闭</p>
     */
    private static final ThreadLocal<Boolean> USER_SHARED_QUERY = new ThreadLocal<>();

    /**
     * 当前线程是否仅查询本人数据
     * <p>默认为 null 时表示否</p>
     */
    private static final ThreadLocal<Boolean> USER_SHARED_QUERY_ONLY_MYSELF = new ThreadLocal<>();

    /**
     * 当前线程是否开启共享查询
     */
    public static boolean getUserSharedQuery() {
        Boolean bool = USER_SHARED_QUERY.get();
        return bool != null && bool;
    }

    /**
     * 设置共享查询开关
     */
    public static void setUserSharedQuery(Boolean sharedQuery) {
        USER_SHARED_QUERY.set(sharedQuery);
    }

    /**
     * 清除当前线程共享查询开关
     */
    public static void removeUserSharedQuery() {
        USER_SHARED_QUERY.remove();
    }

    /**
     * 当前线程是否仅查询本人数据
     */
    public static boolean getUserSharedQueryOnlyMyself() {
        Boolean bool = USER_SHARED_QUERY_ONLY_MYSELF.get();
        return bool != null && bool;
    }

    /**
     * 设置仅查询本人数据开关
     */
    public static void setUserSharedQueryOnlyMyself(Boolean sharedQueryOnlyMyself) {
        USER_SHARED_QUERY_ONLY_MYSELF.set(sharedQueryOnlyMyself);
    }

    /**
     * 清除当前线程仅查询本人数据开关
     */
    public static void removeUserSharedQueryOnlyMyself() {
        USER_SHARED_QUERY_ONLY_MYSELF.remove();
    }
}
