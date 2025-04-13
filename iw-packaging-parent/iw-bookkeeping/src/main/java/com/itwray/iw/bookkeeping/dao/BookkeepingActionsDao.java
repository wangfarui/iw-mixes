package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.model.entity.BookkeepingActionsEntity;
import com.itwray.iw.bookkeeping.mapper.BookkeepingActionsMapper;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 记账行为表 DAO
 *
 * @author wray
 * @since 2025-04-08
 */
@Component
public class BookkeepingActionsDao extends BaseDao<BookkeepingActionsMapper, BookkeepingActionsEntity> {

}
