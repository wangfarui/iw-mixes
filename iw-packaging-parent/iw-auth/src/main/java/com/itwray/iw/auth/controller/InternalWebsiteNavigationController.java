package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.vo.WebsiteNavigationListVo;
import com.itwray.iw.auth.service.BaseWebsiteNavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网站导航内部接口
 *
 * @author wray
 * @since 2026/3/2
 */
@RestController
@RequestMapping("/internal/website/navigation")
@Validated
@Tag(name = "网站导航内部接口")
public class InternalWebsiteNavigationController {

    private final BaseWebsiteNavigationService baseWebsiteNavigationService;

    @Autowired
    public InternalWebsiteNavigationController(BaseWebsiteNavigationService baseWebsiteNavigationService) {
        this.baseWebsiteNavigationService = baseWebsiteNavigationService;
    }

    @GetMapping("/sharedList")
    @Operation(summary = "查询共享网站列表")
    public List<WebsiteNavigationListVo> sharedList() {
        return baseWebsiteNavigationService.querySharedWebsiteList();
    }
}
