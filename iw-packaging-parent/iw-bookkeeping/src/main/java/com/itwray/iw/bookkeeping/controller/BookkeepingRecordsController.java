package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.*;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordsStatisticsVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.consume.BookkeepingRecordsYearStatisticsConsumeVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.income.BookkeepingRecordsYearStatisticsIncomeVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsYearStatisticsOverviewVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.web.annotation.SharedQueryScope;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 记账记录 接口控制层
 *
 * @author wray
 * @since 2024/7/15
 */
@RestController
@RequestMapping("/bookkeeping/records")
@Validated
@Tag(name = "记账记录接口")
public class BookkeepingRecordsController extends WebController<BookkeepingRecordsService, BookkeepingRecordAddDto,
        BookkeepingRecordUpdateDto, BookkeepingRecordDetailVo, Integer> {

    @Autowired
    public BookkeepingRecordsController(BookkeepingRecordsService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询记账记录")
    @SharedQueryScope
    public PageVo<BookkeepingRecordPageVo> page(@RequestBody @Valid BookkeepingRecordPageDto dto) {
        return getWebService().page(dto);
    }

    @PostMapping("/list")
    @Operation(summary = "列表查询记账记录")
    @SharedQueryScope
    public List<BookkeepingRecordPageVo> list(@RequestBody BookkeepingRecordListDto dto) {
        return getWebService().list(dto);
    }

    @PostMapping("/statistics")
    @Operation(summary = "查询记账统计信息")
    @SharedQueryScope
    public BookkeepingRecordsStatisticsVo statistics(@RequestBody BookkeepingRecordsStatisticsDto dto) {
        return getWebService().statistics(dto);
    }

    @Override
    @GetMapping("/detail")
    @Operation(summary = "查询记账记录详情")
    @SharedQueryScope
    public BookkeepingRecordDetailVo detail(@RequestParam("id") Integer id) {
        return super.detail(id);
    }

    @PostMapping("/import")
    @Operation(summary = "导入账单")
    public void importRecords(@RequestParam("file") MultipartFile file) {
        getWebService().importRecords(file);
    }

    @PostMapping("/yearStatistics/overview")
    @Operation(summary = "年度统计-总览")
    @SharedQueryScope
    public BookkeepingRecordsYearStatisticsOverviewVo yearStatisticsOverview(@RequestBody BookkeepingRecordsYearStatisticsQueryDto dto) {
        return getWebService().yearStatisticsOverview(dto);
    }

    @PostMapping("/yearStatistics/consume")
    @Operation(summary = "年度统计-支出")
    @SharedQueryScope
    public BookkeepingRecordsYearStatisticsConsumeVo yearStatisticsConsume(@RequestBody BookkeepingRecordsYearStatisticsQueryDto dto) {
        return getWebService().yearStatisticsConsume(dto);
    }

    @PostMapping("/yearStatistics/income")
    @Operation(summary = "年度统计-收入")
    @SharedQueryScope
    public BookkeepingRecordsYearStatisticsIncomeVo yearStatisticsIncome(@RequestBody BookkeepingRecordsYearStatisticsQueryDto dto) {
        return getWebService().yearStatisticsIncome(dto);
    }
}
