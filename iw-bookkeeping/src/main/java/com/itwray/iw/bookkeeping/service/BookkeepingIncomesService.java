package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.IncomesAddDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesPageDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.IncomesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.IncomesPageVo;
import com.itwray.iw.web.model.PageVo;

/**
 * 收入表 接口服务
 *
 * @author wray
 * @since 2024/7/15
 */
public interface BookkeepingIncomesService {

    void add(IncomesAddDto dto);

    void update(IncomesUpdateDto dto);

    void delete(Integer id);

    PageVo<IncomesPageVo> page(IncomesPageDto dto);

    IncomesDetailVo detail(Integer id);
}
