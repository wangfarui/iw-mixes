package com.itwray.iw.web.model.dto;

/**
 * 共享查询请求
 *
 * @author wray
 * @since 2026/3/18
 */
public interface SharedQueryRequest {

    /**
     * 是否仅查询本人数据
     */
    Integer getQueryOnlyMyself();
}
