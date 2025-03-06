package com.itwray.iw.auth.dao;

import com.itwray.iw.auth.model.entity.BaseApplicationAccountEntity;
import com.itwray.iw.auth.mapper.BaseApplicationAccountMapper;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 应用账号信息表 DAO
 *
 * @author wray
 * @since 2025-03-06
 */
@Component
public class BaseApplicationAccountDao extends BaseDao<BaseApplicationAccountMapper, BaseApplicationAccountEntity> {

}
