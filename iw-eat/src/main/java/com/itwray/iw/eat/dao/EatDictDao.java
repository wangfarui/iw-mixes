package com.itwray.iw.eat.dao;

import com.itwray.iw.eat.mapper.EatDictMapper;
import com.itwray.iw.eat.model.entity.EatDictEntity;
import com.itwray.iw.web.mybatis.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 字典表 DAO
 *
 * @author wray
 * @since 2024/5/26
 */
@Component
public class EatDictDao extends BaseDao<EatDictMapper, EatDictEntity> {
}
