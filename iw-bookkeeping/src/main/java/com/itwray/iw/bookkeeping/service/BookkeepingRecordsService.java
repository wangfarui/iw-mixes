package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.web.model.PageVo;

import java.util.List;

/**
 * 收入表 接口服务
 *
 * @author wray
 * @since 2024/8/28
 */
public interface BookkeepingRecordsService {

    void add(BookkeepingRecordAddDto dto);

    void update(BookkeepingRecordUpdateDto dto);

    void delete(Integer id);

    PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto);

    BookkeepingRecordDetailVo detail(Integer id);

    List<BookkeepingRecordPageVo> list(BookkeepingRecordListDto dto);
}
