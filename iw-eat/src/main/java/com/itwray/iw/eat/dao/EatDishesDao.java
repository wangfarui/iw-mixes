package com.itwray.iw.eat.dao;

import com.itwray.iw.eat.mapper.EatDishesMapper;
import com.itwray.iw.eat.model.entity.EatDishesEntity;
import com.itwray.iw.web.mybatis.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 菜品表 DAO
 *
 * @author wray
 * @since 2024-04-23
 */
@Component
public class EatDishesDao extends BaseDao<EatDishesMapper, EatDishesEntity> {

}
