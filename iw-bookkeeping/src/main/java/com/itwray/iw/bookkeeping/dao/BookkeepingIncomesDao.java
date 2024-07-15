package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingIncomesMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingIncomesEntity;
import com.itwray.iw.web.mybatis.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 收入表 DAO
 *
 * @author wray
 * @since 2024/7/15
 */
@Component
public class BookkeepingIncomesDao extends BaseDao<BookkeepingIncomesMapper, BookkeepingIncomesEntity> {
}
