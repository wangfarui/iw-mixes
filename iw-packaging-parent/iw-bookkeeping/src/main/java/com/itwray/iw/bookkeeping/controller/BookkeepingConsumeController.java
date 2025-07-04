package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeStatisticsDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账支出 接口控制层
 *
 * @author wray
 * @since 2024/10/15
 */
@RestController
@RequestMapping("/bookkeeping/consume")
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
    public BookkeepingStatisticsTotalVo totalStatistics(@RequestBody @Valid BookkeepingConsumeStatisticsDto dto) {
        return bookkeepingConsumeService.totalStatistics(dto);
    }

    @PostMapping("/rankStatistics")
    @Operation(summary = "支出排行统计")
    public List<BookkeepingStatisticsRankVo> rankStatistics(@RequestBody @Valid BookkeepingConsumeStatisticsDto dto) {
        return bookkeepingConsumeService.rankStatistics(dto);
    }

    @PostMapping("/pieChartStatistics")
    @Operation(summary = "支出饼图图表统计")
    public List<BookkeepingConsumeStatisticsCategoryVo> pieChartStatistics(@RequestBody @Valid BookkeepingConsumeCategoryStatisticsDto dto) {
        return bookkeepingConsumeService.pieChartStatistics(dto);
    }

    @PostMapping("/barChartStatistics")
    @Operation(summary = "支出柱状图图表统计")
    public List<BigDecimal> barChartStatistics(@RequestBody @Valid BookkeepingConsumeStatisticsDto dto) {
        return bookkeepingConsumeService.barChartStatistics(dto);
    }
}
