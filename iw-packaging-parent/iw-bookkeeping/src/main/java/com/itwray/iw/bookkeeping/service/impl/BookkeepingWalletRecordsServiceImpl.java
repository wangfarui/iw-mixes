package com.itwray.iw.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.bookkeeping.dao.BookkeepingWalletRecordsDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletRecordsPageDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletRecordsEntity;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletRecordsPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletRecordsService;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户钱包记录表 服务实现类
 *
 * @author wray
 * @since 2025-05-26
 */
@Service
public class BookkeepingWalletRecordsServiceImpl implements BookkeepingWalletRecordsService {

    private final BookkeepingWalletRecordsDao bookkeepingWalletRecordsDao;

    @Autowired
    public BookkeepingWalletRecordsServiceImpl(BookkeepingWalletRecordsDao bookkeepingWalletRecordsDao) {
        this.bookkeepingWalletRecordsDao = bookkeepingWalletRecordsDao;
    }

    @Override
    public PageVo<BookkeepingWalletRecordsPageVo> page(BookkeepingWalletRecordsPageDto dto) {
        LambdaQueryWrapper<BookkeepingWalletRecordsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookkeepingWalletRecordsEntity::getUserId, UserUtils.getUserId())
                .eq(BookkeepingWalletRecordsEntity::getChangeType, dto.getChangeType())
                .orderByDesc(BookkeepingWalletRecordsEntity::getId);
        return bookkeepingWalletRecordsDao.page(dto, queryWrapper, BookkeepingWalletRecordsPageVo.class);
    }
}
