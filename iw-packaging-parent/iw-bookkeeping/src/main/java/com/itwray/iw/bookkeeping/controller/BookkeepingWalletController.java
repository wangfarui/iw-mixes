package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletAmountUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletDetailVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/bookkeeping/wallet")
@Validated
@Tag(name = "用户钱包表接口")
public class BookkeepingWalletController {

    private final BookkeepingWalletService bookkeepingWalletService;

    @Autowired
    public BookkeepingWalletController(BookkeepingWalletService bookkeepingWalletService) {
        this.bookkeepingWalletService = bookkeepingWalletService;
    }

    @GetMapping("/detail")
    @Operation(summary = "查询用户钱包详情")
    public BookkeepingWalletDetailVo getUserWalletDetail() {
        return bookkeepingWalletService.getUserWalletDetail();
    }

    @PutMapping("/updateAmount")
    @Operation(summary = "修改钱包金额")
    public void updateBalance(@RequestBody @Valid BookkeepingWalletAmountUpdateDto dto) {
        bookkeepingWalletService.updateAmount(dto);
    }
}
