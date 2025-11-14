package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionListVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 会员订阅记录表 服务接口
 *
 * @author wray
 * @since 2025/11/5
 */
public interface BookkeepingMembershipSubscriptionService extends WebService<BookkeepingMembershipSubscriptionAddDto, BookkeepingMembershipSubscriptionUpdateDto, BookkeepingMembershipSubscriptionDetailVo, Integer> {

    List<BookkeepingMembershipSubscriptionListVo> list(BookkeepingMembershipSubscriptionListDto dto);
}
