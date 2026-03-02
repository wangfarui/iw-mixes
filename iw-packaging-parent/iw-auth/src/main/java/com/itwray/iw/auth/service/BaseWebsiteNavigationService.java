package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.WebsiteNavigationAddDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationPageDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationUpdateDto;
import com.itwray.iw.auth.model.vo.WebsiteNavigationListVo;
import com.itwray.iw.auth.model.vo.WebsiteNavigationDetailVo;
import com.itwray.iw.auth.model.vo.WebsiteNavigationPageVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 网站导航记录 服务接口
 *
 * @author wray
 * @since 2026-02-28
 */
public interface BaseWebsiteNavigationService extends WebService<WebsiteNavigationAddDto, WebsiteNavigationUpdateDto, WebsiteNavigationDetailVo, Integer> {

    PageVo<WebsiteNavigationPageVo> page(WebsiteNavigationPageDto dto);

    List<WebsiteNavigationListVo> querySharedWebsiteList();
}
