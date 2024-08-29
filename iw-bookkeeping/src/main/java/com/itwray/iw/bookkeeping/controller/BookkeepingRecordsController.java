package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.web.model.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class BookkeepingRecordsController {

    @Resource
    private BookkeepingRecordsService bookkeepingRecordsService;

    @PostMapping("/add")
    @Operation(summary = "新增记账记录")
    public void add(@RequestBody @Valid BookkeepingRecordAddDto dto) {
        bookkeepingRecordsService.add(dto);
    }

    @PostMapping("/update")
    @Operation(summary = "更新记账记录")
    public void update(@RequestBody @Valid BookkeepingRecordUpdateDto dto) {
        bookkeepingRecordsService.update(dto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除记账记录")
    public void delete(@RequestParam Integer id) {
        bookkeepingRecordsService.delete(id);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询记账记录")
    public PageVo<BookkeepingRecordPageVo> page(@RequestBody @Valid BookkeepingRecordPageDto dto) {
        return bookkeepingRecordsService.page(dto);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询记账记录详情")
    public BookkeepingRecordDetailVo detail(@RequestParam("id") Integer id) {
        return bookkeepingRecordsService.detail(id);
    }

    @PostMapping("/list")
    @Operation(summary = "列表查询记账记录")
    public List<BookkeepingRecordPageVo> list(@RequestBody BookkeepingRecordListDto dto) {
        return bookkeepingRecordsService.list(dto);
    }
}
