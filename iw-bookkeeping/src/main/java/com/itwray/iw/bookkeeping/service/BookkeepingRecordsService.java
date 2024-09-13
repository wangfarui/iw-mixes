package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 收入表 接口服务
 *
 * @author wray
 * @since 2024/8/28
 */
public interface BookkeepingRecordsService extends WebService {

    PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto);

    List<BookkeepingRecordPageVo> list(BookkeepingRecordListDto dto);
}
