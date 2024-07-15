package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.IncomesAddDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesPageDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.IncomesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.IncomesPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingIncomesService;
import com.itwray.iw.web.model.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 记账收入 接口控制层
 *
 * @author wray
 * @since 2024/7/15
 */
@RestController
@RequestMapping("/incomes")
@Validated
@Tag(name = "记账收入接口")
public class BookkeepingIncomesController {

    @Resource
    private BookkeepingIncomesService bookkeepingIncomesService;

    @PostMapping("/add")
    @Operation(summary = "新增收入信息")
    public void add(@RequestBody @Valid IncomesAddDto dto) {
        bookkeepingIncomesService.add(dto);
    }

    @PostMapping("/update")
    @Operation(summary = "更新收入信息")
    public void update(@RequestBody @Valid IncomesUpdateDto dto) {
        bookkeepingIncomesService.update(dto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收入信息")
    public void delete(@RequestParam Integer id) {
        bookkeepingIncomesService.delete(id);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询收入信息")
    public PageVo<IncomesPageVo> page(@RequestBody @Valid IncomesPageDto dto) {
        return bookkeepingIncomesService.page(dto);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询收入信息详情")
    public IncomesDetailVo detail(@RequestParam("id") Integer id) {
        return bookkeepingIncomesService.detail(id);
    }
}
