package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletBalanceUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletBalanceVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户钱包表 接口控制层
 *
 * @author wray
 * @since 2025-05-22
 */
@RestController
@RequestMapping("/wallet")
@Validated
@Tag(name = "用户钱包表接口")
public class BookkeepingWalletController {

    private final BookkeepingWalletService bookkeepingWalletService;

    @Autowired
    public BookkeepingWalletController(BookkeepingWalletService bookkeepingWalletService) {
        this.bookkeepingWalletService = bookkeepingWalletService;
    }

    @GetMapping("/balance")
    @Operation(summary = "查询钱包余额")
    public BookkeepingWalletBalanceVo getWalletBalance() {
        return bookkeepingWalletService.getWalletBalance();
    }

    @PutMapping("/updateBalance")
    @Operation(summary = "修改钱包余额")
    public void updateBalance(@RequestBody BookkeepingWalletBalanceUpdateDto dto) {
        bookkeepingWalletService.updateBalance(dto);
    }
}
