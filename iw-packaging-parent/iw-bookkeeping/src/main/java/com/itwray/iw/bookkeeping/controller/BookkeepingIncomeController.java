package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingIncomeStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
import com.itwray.iw.bookkeeping.service.BookkeepingIncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账收入 接口控制层
 *
 * @author wray
 * @since 2025/05/27
 */
@RestController
@RequestMapping("/bookkeeping/income")
@Validated
@Tag(name = "记账收入接口")
public class BookkeepingIncomeController {

    private final BookkeepingIncomeService bookkeepingIncomeService;

    @Autowired
    public BookkeepingIncomeController(BookkeepingIncomeService bookkeepingIncomeService) {
        this.bookkeepingIncomeService = bookkeepingIncomeService;
    }

    @PostMapping("/totalStatistics")
    @Operation(summary = "收入总统计")
    public BookkeepingStatisticsTotalVo totalStatistics(@RequestBody @Valid BookkeepingIncomeStatisticsDto dto) {
        return bookkeepingIncomeService.totalStatistics(dto);
    }

    @PostMapping("/rankStatistics")
    @Operation(summary = "收入排行统计")
    public List<BookkeepingStatisticsRankVo> rankStatistics(@RequestBody @Valid BookkeepingIncomeStatisticsDto dto) {
        return bookkeepingIncomeService.rankStatistics(dto);
    }

    @PostMapping("/chartStatistics")
    @Operation(summary = "收入图表统计")
    public List<BigDecimal> chartStatistics(@RequestBody @Valid BookkeepingIncomeStatisticsDto dto) {
        return bookkeepingIncomeService.chartStatistics(dto);
    }
}
