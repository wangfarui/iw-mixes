package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.dto.EatFridgeFoodAddDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodPageDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodUpdateDto;
import com.itwray.iw.eat.model.vo.EatFridgeFoodDetailVo;
import com.itwray.iw.eat.model.vo.EatFridgeFoodPageVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

/**
 * 冰箱食材表 服务接口
 *
 * @author wray
 * @since 2026-01-20
 */
public interface EatFridgeFoodService extends WebService<EatFridgeFoodAddDto, EatFridgeFoodUpdateDto, EatFridgeFoodDetailVo, Integer> {

    PageVo<EatFridgeFoodPageVo> page(EatFridgeFoodPageDto dto);

    void markAsUsed(Integer id);
}
