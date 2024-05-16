package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.dto.MealAddDto;
import com.itwray.iw.eat.model.dto.MealPageDto;
import com.itwray.iw.eat.model.dto.MealUpdateDto;
import com.itwray.iw.eat.model.vo.MealDetailVo;
import com.itwray.iw.eat.model.vo.MealDishesMaterialDetailVo;
import com.itwray.iw.eat.model.vo.MealPageVo;
import com.itwray.iw.web.model.PageVo;

/**
 * 用餐表 服务类
 *
 * @author wray
 * @since 2024-04-23
 */
public interface EatMealService {

    /**
     * 新增用餐记录
     *
     * @param dto 用餐新增对象
     * @return 用餐记录id
     */
    Integer add(MealAddDto dto);

    /**
     * 修改用餐记录
     *
     * @param dto 用餐修改对象
     */
    void update(MealUpdateDto dto);

    /**
     * 删除用餐记录
     *
     * @param id 用餐记录id
     */
    void delete(Integer id);

    /**
     * 分页查询用餐记录
     *
     * @param dto 分页查询对象
     * @return 分页数据
     */
    PageVo<MealPageVo> page(MealPageDto dto);

    /**
     * 查询用餐详情信息
     *
     * @param id 用餐记录id
     * @return 用餐详情信息
     */
    MealDetailVo detail(Integer id);

    /**
     * 查询用餐记录的菜品食材详情信息
     *
     * @param mealId 用餐记录id
     * @return 菜品食材详情信息
     */
    MealDishesMaterialDetailVo dishesMaterialDetail(Integer mealId);

}
