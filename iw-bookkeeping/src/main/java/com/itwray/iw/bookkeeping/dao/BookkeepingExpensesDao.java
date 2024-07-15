package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingExpensesMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingExpensesEntity;
import com.itwray.iw.web.mybatis.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 支出表 DAO
 *
 * @author wray
 * @since 2024/7/15
 */
@Component
public class BookkeepingExpensesDao extends BaseDao<BookkeepingExpensesMapper, BookkeepingExpensesEntity> {
}
