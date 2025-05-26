package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletRecordsPageDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletRecordsPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletRecordsService;
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

/**
 * 用户钱包记录表 接口控制层
 *
 * @author wray
 * @since 2025-05-26
 */
@RestController
@RequestMapping("/bookkeeping/wallet/records")
@Validated
@Tag(name = "用户钱包记录表接口")
public class BookkeepingWalletRecordsController {

    private final BookkeepingWalletRecordsService bookkeepingWalletRecordsService;

    @Autowired
    public BookkeepingWalletRecordsController(BookkeepingWalletRecordsService bookkeepingWalletRecordsService) {
        this.bookkeepingWalletRecordsService = bookkeepingWalletRecordsService;
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询钱包金额变动记录列表")
    public PageVo<BookkeepingWalletRecordsPageVo> page(@RequestBody @Valid BookkeepingWalletRecordsPageDto dto) {
        return bookkeepingWalletRecordsService.page(dto);
    }
}
