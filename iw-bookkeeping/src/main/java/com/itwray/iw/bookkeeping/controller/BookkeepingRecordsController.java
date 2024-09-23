package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.*;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordsStatisticsVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
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

import java.util.List;

/**
 * 记账记录 接口控制层
 *
 * @author wray
 * @since 2024/7/15
 */
@RestController
@RequestMapping("/records")
@Validated
@Tag(name = "记账记录接口")
public class BookkeepingRecordsController extends WebController<BookkeepingRecordsService, BookkeepingRecordAddDto,
        BookkeepingRecordUpdateDto, BookkeepingRecordDetailVo> {

    @Autowired
    public BookkeepingRecordsController(BookkeepingRecordsService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询记账记录")
    public PageVo<BookkeepingRecordPageVo> page(@RequestBody @Valid BookkeepingRecordPageDto dto) {
        return getWebService().page(dto);
    }

    @PostMapping("/list")
    @Operation(summary = "列表查询记账记录")
    public List<BookkeepingRecordPageVo> list(@RequestBody BookkeepingRecordListDto dto) {
        return getWebService().list(dto);
    }

    @PostMapping("/statistics")
    @Operation(summary = "查询记账统计信息")
    public BookkeepingRecordsStatisticsVo statistics(@RequestBody BookkeepingRecordsStatisticsDto dto) {
        return getWebService().statistics(dto);
    }
}
