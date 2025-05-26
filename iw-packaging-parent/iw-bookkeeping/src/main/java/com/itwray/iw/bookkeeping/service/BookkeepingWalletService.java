package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletAmountUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletDetailVo;

/**
 * 用户钱包表 服务接口
 *
 * @author wray
 * @since 2025-05-22
 */
public interface BookkeepingWalletService {

    BookkeepingWalletDetailVo getUserWalletDetail();

    void updateAmount(BookkeepingWalletAmountUpdateDto dto);
}
