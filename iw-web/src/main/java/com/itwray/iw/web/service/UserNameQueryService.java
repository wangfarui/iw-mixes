package com.itwray.iw.web.service;

import java.util.Collection;
import java.util.Map;

/**
 * 用户姓名查询服务
 *
 * @author wray
 * @since 2026/3/19
 */
public interface UserNameQueryService {

    /**
     * 批量查询用户姓名映射
     *
     * @param userIdCollection 用户id集合
     * @return key -> 用户id, value -> 用户姓名
     */
    Map<Integer, String> queryUserNameMap(Collection<Integer> userIdCollection);
}
