package com.itwray.iw.auth.dao;

import com.itwray.iw.auth.mapper.BaseAiTaskMapper;
import com.itwray.iw.auth.model.entity.BaseAiTaskEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * AI任务表DAO
 *
 * @author wray
 * @since 2026-03-26
 */
@Component
public class BaseAiTaskDao extends BaseDao<BaseAiTaskMapper, BaseAiTaskEntity> {
}
