package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingAssistantConfirmExpenseDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantConfirmExpenseVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantParseExpenseVo;
import com.itwray.iw.bookkeeping.service.BookkeepingAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 记账助手接口控制层
 *
 * @author wray
 * @since 2026/4/14
 */
@RestController
@RequestMapping("/bookkeeping/assistant")
@Validated
@Tag(name = "记账助手接口")
public class BookkeepingAssistantController {

    private final BookkeepingAssistantService bookkeepingAssistantService;

    public BookkeepingAssistantController(BookkeepingAssistantService bookkeepingAssistantService) {
        this.bookkeepingAssistantService = bookkeepingAssistantService;
    }

    @PostMapping("/expense/parseAudio")
    @Operation(summary = "解析语音支出记账草稿")
    public BookkeepingAssistantParseExpenseVo parseExpenseAudio(@RequestParam("file") MultipartFile file,
                                                                @RequestParam(value = "durationMs", required = false) Integer durationMs,
                                                                @RequestParam(value = "format", required = false) String format,
                                                                @RequestParam(value = "sampleRate", required = false) Integer sampleRate,
                                                                @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
        return bookkeepingAssistantService.parseExpenseAudio(file, durationMs, format, sampleRate, autoSave);
    }

    @PostMapping("/expense/confirm")
    @Operation(summary = "确认语音支出记账草稿并保存")
    public BookkeepingAssistantConfirmExpenseVo confirmExpense(@RequestBody @Valid BookkeepingAssistantConfirmExpenseDto dto) {
        return bookkeepingAssistantService.confirmExpense(dto);
    }
}
