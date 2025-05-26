package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletRecordsPageDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletRecordsPageVo;
import com.itwray.iw.web.model.vo.PageVo;

/**
 * 用户钱包记录表 服务接口
 *
 * @author wray
 * @since 2025-05-26
 */
public interface BookkeepingWalletRecordsService {

    PageVo<BookkeepingWalletRecordsPageVo> page(BookkeepingWalletRecordsPageDto dto);
}
