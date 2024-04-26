package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.dto.DishesPageDto;
import com.itwray.iw.eat.model.dto.DishesAddDto;
import com.itwray.iw.eat.model.dto.DishesUpdateDto;
import com.itwray.iw.eat.model.vo.DishesDetailVo;
import com.itwray.iw.eat.model.vo.DishesPageVo;
import com.itwray.iw.web.model.PageVo;

/**
 * 菜品表 服务类
 *
 * @author wray
 * @since 2024-04-23
 */
public interface EatDishesService {

    /**
     * 新增菜品
     *
     * @param dto 菜品新增对象
     * @return 菜品id
     */
    Integer add(DishesAddDto dto);

    /**
     * 修改菜品
     *
     * @param dto 菜品修改对象
     */
    void update(DishesUpdateDto dto);

    /**
     * 删除菜品
     *
     * @param id 菜品id
     */
    void delete(Integer id);

    /**
     * 分页查询菜品
     *
     * @param dto 分页查询对象
     * @return 分页数据
     */
    PageVo<DishesPageVo> page(DishesPageDto dto);

    /**
     * 查询菜品详情信息
     *
     * @param id 菜品记录id
     * @return 菜品详情信息
     */
    DishesDetailVo detail(Integer id);

}
