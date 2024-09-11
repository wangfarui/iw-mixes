package com.itwray.iw.eat.dao;

import com.itwray.iw.eat.mapper.EatMealMapper;
import com.itwray.iw.eat.model.entity.EatMealEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 用餐表 DAO
 *
 * @author wray
 * @since 2024-04-23
 */
@Component
public class EatMealDao extends BaseDao<EatMealMapper, EatMealEntity> {

}
