package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionListVo;
import com.itwray.iw.bookkeeping.service.BookkeepingMembershipSubscriptionService;
import com.itwray.iw.web.controller.WebController;
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

import java.util.List;

/**
 * 会员订阅记录表 接口控制层
 *
 * @author wray
 * @since 2025/11/5
 */
@RestController
@RequestMapping("/bookkeeping/membership")
@Validated
@Tag(name = "会员订阅记录表接口")
public class BookkeepingMembershipSubscriptionController extends WebController<BookkeepingMembershipSubscriptionService,
        BookkeepingMembershipSubscriptionAddDto, BookkeepingMembershipSubscriptionUpdateDto, BookkeepingMembershipSubscriptionDetailVo, Integer> {

    @Autowired
    public BookkeepingMembershipSubscriptionController(BookkeepingMembershipSubscriptionService webService) {
        super(webService);
    }

    @PostMapping("/list")
    @Operation(summary = "查询会员订阅记录列表")
    public List<BookkeepingMembershipSubscriptionListVo> list(@RequestBody @Valid BookkeepingMembershipSubscriptionListDto dto) {
        return getWebService().list(dto);
    }
}
