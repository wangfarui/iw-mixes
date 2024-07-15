package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.ExpensesAddDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesPageDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.ExpensesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.ExpensesPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingExpensesService;
import com.itwray.iw.web.model.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 记账支出 接口控制层
 *
 * @author wray
 * @since 2024/7/15
 */
@RestController
@RequestMapping("/expenses")
@Validated
@Tag(name = "记账支出接口")
public class BookkeepingExpensesController {

    @Resource
    private BookkeepingExpensesService bookkeepingExpensesService;

    @PostMapping("/add")
    @Operation(summary = "新增支出信息")
    public void add(@RequestBody @Valid ExpensesAddDto dto) {
        bookkeepingExpensesService.add(dto);
    }

    @PostMapping("/update")
    @Operation(summary = "更新支出信息")
    public void update(@RequestBody @Valid ExpensesUpdateDto dto) {
        bookkeepingExpensesService.update(dto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除支出信息")
    public void delete(@RequestParam Integer id) {
        bookkeepingExpensesService.delete(id);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询支出信息")
    public PageVo<ExpensesPageVo> page(@RequestBody @Valid ExpensesPageDto dto) {
        return bookkeepingExpensesService.page(dto);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询支出信息详情")
    public ExpensesDetailVo detail(@RequestParam("id") Integer id) {
        return bookkeepingExpensesService.detail(id);
    }
}
