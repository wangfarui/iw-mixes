package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingWalletMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletEntity;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.exception.IwWebException;
import org.springframework.stereotype.Component;

/**
 * 用户钱包表 DAO
 *
 * @author wray
 * @since 2025-05-22
 */
@Component
public class BookkeepingWalletDao extends BaseDao<BookkeepingWalletMapper, BookkeepingWalletEntity> {

    public BookkeepingWalletEntity queryByUserId(Integer userId) {
        BookkeepingWalletEntity entity = this.lambdaQuery()
                .eq(BookkeepingWalletEntity::getUserId, userId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        // 正常情况下不会出现用户没有钱包数据的情况
        if (entity == null) {
            throw new IwWebException("用户钱包为空, userId: " + userId);
        }
        return entity;
    }
}
