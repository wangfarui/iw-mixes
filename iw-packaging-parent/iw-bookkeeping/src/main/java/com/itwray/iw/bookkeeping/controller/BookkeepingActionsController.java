package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingActionsDetailVo;
import com.itwray.iw.bookkeeping.service.BookkeepingActionsService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 记账行为表 接口控制层
 *
 * @author wray
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/bookkeeping/actions")
@Validated
@Tag(name = "记账行为表接口")
public class BookkeepingActionsController extends WebController<BookkeepingActionsService,
        BookkeepingActionsAddDto, BookkeepingActionsUpdateDto, BookkeepingActionsDetailVo, Integer>  {

    @Autowired
    public BookkeepingActionsController(BookkeepingActionsService webService) {
        super(webService);
    }

    @GetMapping("/list")
    @Operation(summary = "获取记账行为")
    public List<BookkeepingActionsDetailVo> list(@RequestParam("recordCategory") Integer recordCategory) {
        return getWebService().list(recordCategory);
    }
}
