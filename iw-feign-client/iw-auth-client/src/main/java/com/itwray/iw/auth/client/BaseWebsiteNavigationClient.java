package com.itwray.iw.auth.client;

import com.itwray.iw.auth.model.vo.WebsiteNavigationListVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 网站导航接口
 *
 * @author wray
 * @since 2026/3/2
 */
@FeignClient(value = "iw-auth-service", contextId = "websiteNavigationClient", path = "/internal/website/navigation")
public interface BaseWebsiteNavigationClient {

    @GetMapping("/sharedList")
    @Operation(summary = "查询共享网站列表")
    List<WebsiteNavigationListVo> querySharedWebsiteList();
}
