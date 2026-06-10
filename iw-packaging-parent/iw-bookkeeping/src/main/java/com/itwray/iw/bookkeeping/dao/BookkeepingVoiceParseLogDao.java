package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingVoiceParseLogMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingVoiceParseLogEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 语音记账解析日志 DAO
 *
 * @author wray
 * @since 2026/4/14
 */
@Component
public class BookkeepingVoiceParseLogDao extends BaseDao<BookkeepingVoiceParseLogMapper, BookkeepingVoiceParseLogEntity> {
}
