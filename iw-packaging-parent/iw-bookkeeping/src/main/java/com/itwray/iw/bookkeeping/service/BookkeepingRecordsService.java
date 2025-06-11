package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.bo.BookkeepingRecordsImportBo;
import com.itwray.iw.bookkeeping.model.dto.*;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordsStatisticsVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 收入表 服务接口
 *
 * @author wray
 * @since 2024/8/28
 */
public interface BookkeepingRecordsService extends WebService<BookkeepingRecordAddDto, BookkeepingRecordUpdateDto, BookkeepingRecordDetailVo, Integer> {

    PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto);

    List<BookkeepingRecordPageVo> list(BookkeepingRecordListDto dto);

    BookkeepingRecordsStatisticsVo statistics(BookkeepingRecordsStatisticsDto dto);

    void importRecords(MultipartFile file);

    void processImportData(BookkeepingRecordsImportBo bo, Map<String, Integer> dictNameMap);
}
