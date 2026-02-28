package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.WebsiteNavigationAddDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationPageDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationUpdateDto;
import com.itwray.iw.auth.model.vo.WebsiteNavigationDetailVo;
import com.itwray.iw.auth.model.vo.WebsiteNavigationPageVo;
import com.itwray.iw.auth.service.BaseWebsiteNavigationService;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站导航记录 接口控制层
 *
 * @author wray
 * @since 2026-02-28
 */
@RestController
@RequestMapping("/website/navigation")
@Validated
@Tag(name = "网站导航记录接口")
public class BaseWebsiteNavigationController extends WebController<BaseWebsiteNavigationService,
        WebsiteNavigationAddDto, WebsiteNavigationUpdateDto, WebsiteNavigationDetailVo, Integer> {

    @Autowired
    public BaseWebsiteNavigationController(BaseWebsiteNavigationService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询网站导航记录")
    public PageVo<WebsiteNavigationPageVo> page(@RequestBody @Valid WebsiteNavigationPageDto dto) {
        return getWebService().page(dto);
    }
}
