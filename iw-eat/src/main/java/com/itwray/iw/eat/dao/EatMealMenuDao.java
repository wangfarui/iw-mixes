package com.itwray.iw.eat.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.eat.mapper.EatMealMenuMapper;
import com.itwray.iw.eat.model.dto.MealMenuAddDto;
import com.itwray.iw.eat.model.entity.EatMealMenuEntity;
import com.itwray.iw.eat.model.vo.MealMenuDetailVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用餐菜单表 DAO
 *
 * @author wray
 * @since 2024-04-23
 */
@Component
public class EatMealMenuDao extends ServiceImpl<EatMealMenuMapper, EatMealMenuEntity> {

    /**
     * 保存用餐菜单明细
     *
     * @param mealId       用餐记录id
     * @param mealMenuList 菜单明细
     */
    public void saveMealMenu(Integer mealId, List<MealMenuAddDto> mealMenuList) {
        if (CollUtil.isEmpty(mealMenuList)) {
            return;
        }
        List<EatMealMenuEntity> eatMealMenus = mealMenuList.stream().map(dto -> {
            EatMealMenuEntity eatMealMenu = BeanUtil.copyProperties(dto, EatMealMenuEntity.class);
            eatMealMenu.setMealId(mealId);
            return eatMealMenu;
        }).collect(Collectors.toList());
        this.saveBatch(eatMealMenus);
    }

    /**
     * 物理删除菜单明细
     *
     * @param mealId 用餐记录id
     */
    public void removeMealMenuByMealId(Integer mealId) {
        this.lambdaUpdate()
                .eq(EatMealMenuEntity::getMealId, mealId)
                .remove();
    }

    /**
     * 查询菜单明细
     *
     * @param mealId 用餐记录id
     * @return 菜单明细
     */
    public List<MealMenuDetailVo> getListByMealId(Integer mealId) {
        return this.lambdaQuery()
                .eq(EatMealMenuEntity::getMealId, mealId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, MealMenuDetailVo.class))
                .collect(Collectors.toList());
    }
}
