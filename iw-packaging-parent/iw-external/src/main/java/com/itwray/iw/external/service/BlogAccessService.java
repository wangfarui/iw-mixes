package com.itwray.iw.external.service;

import com.itwray.iw.external.model.dto.BlogAccessVerifyDto;
import com.itwray.iw.external.model.vo.BlogAccessVerifyVo;

/**
 * 博客文章访问服务。
 *
 * @author wray
 * @since 2026/6/24
 */
public interface BlogAccessService {

    /**
     * 校验文章访问密码。
     *
     * @param dto       校验参数
     * @param clientIp  客户端IP
     * @param userAgent User-Agent
     * @return 校验结果
     */
    BlogAccessVerifyVo verify(BlogAccessVerifyDto dto, String clientIp, String userAgent);
}
