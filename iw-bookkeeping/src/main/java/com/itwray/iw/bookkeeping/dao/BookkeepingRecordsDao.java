package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 记账记录表 DAO
 *
 * @author wray
 * @since 2024/8/28
 */
@Component
public class BookkeepingRecordsDao extends BaseDao<BookkeepingRecordsMapper, BookkeepingRecordsEntity> {
}
