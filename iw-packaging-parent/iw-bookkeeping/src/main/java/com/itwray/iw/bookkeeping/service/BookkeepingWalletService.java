package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletBalanceVo;

/**
 * 用户钱包表 服务接口
 *
 * @author wray
 * @since 2025-05-22
 */
public interface BookkeepingWalletService {

    BookkeepingWalletBalanceVo getWalletBalance();
}
