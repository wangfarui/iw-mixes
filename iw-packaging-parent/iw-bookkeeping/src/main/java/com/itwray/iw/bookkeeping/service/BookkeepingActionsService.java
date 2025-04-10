package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.web.service.WebService;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingActionsDetailVo;

import java.util.List;

/**
 * 记账行为表 服务接口
 *
 * @author wray
 * @since 2025-04-08
 */
public interface BookkeepingActionsService extends WebService<BookkeepingActionsAddDto, BookkeepingActionsUpdateDto, BookkeepingActionsDetailVo, Integer> {

    List<BookkeepingActionsDetailVo> list(Integer recordCategory);
}
