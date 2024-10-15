package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeMonthStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsTotalVo;
import com.itwray.iw.bookkeeping.service.BookkeepingConsumeService;
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
 * 记账支出 接口控制层
 *
 * @author wray
 * @since 2024/10/15
 */
@RestController
@RequestMapping("/consume")
@Validated
@Tag(name = "记账支出接口")
public class BookkeepingConsumeController {

    private final BookkeepingConsumeService bookkeepingConsumeService;

    @Autowired
    public BookkeepingConsumeController(BookkeepingConsumeService bookkeepingConsumeService) {
        this.bookkeepingConsumeService = bookkeepingConsumeService;
    }

    @PostMapping("/totalStatistics")
    @Operation(summary = "支出总统计")
    public BookkeepingConsumeStatisticsTotalVo totalStatistics(@RequestBody @Valid BookkeepingConsumeMonthStatisticsDto dto) {
        return bookkeepingConsumeService.totalStatistics(dto);
    }

    @PostMapping("/rankStatistics")
    @Operation(summary = "支出排行统计")
    public List<BookkeepingConsumeStatisticsRankVo> rankStatistics(@RequestBody @Valid BookkeepingConsumeMonthStatisticsDto dto) {
        return bookkeepingConsumeService.rankStatistics(dto);
    }

    @PostMapping("/categoryStatistics")
    @Operation(summary = "支出分类统计")
    public List<BookkeepingConsumeStatisticsCategoryVo> categoryStatistics(@RequestBody @Valid BookkeepingConsumeCategoryStatisticsDto dto) {
        return bookkeepingConsumeService.categoryStatistics(dto);
    }
}
