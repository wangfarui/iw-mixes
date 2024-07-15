package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.ExpensesAddDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesPageDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.ExpensesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.ExpensesPageVo;
import com.itwray.iw.web.model.PageVo;

/**
 * 支出表 接口服务
 *
 * @author wray
 * @since 2024/7/15
 */
public interface BookkeepingExpensesService {

    void add(ExpensesAddDto dto);

    void update(ExpensesUpdateDto dto);

    void delete(Integer id);

    PageVo<ExpensesPageVo> page(ExpensesPageDto dto);

    ExpensesDetailVo detail(Integer id);
}
