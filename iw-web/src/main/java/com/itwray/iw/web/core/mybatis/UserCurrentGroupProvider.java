package com.itwray.iw.web.core.mybatis;

/**
 * 当前用户家庭组提供器
 *
 * @author wray
 * @since 2026/3/12
 */
public interface UserCurrentGroupProvider {

    /**
     * 查询用户当前家庭组ID
     *
     * @param userId 用户ID
     * @return 家庭组ID, 0表示个人模式
     */
    Integer queryCurrentGroupId(Integer userId);
}
