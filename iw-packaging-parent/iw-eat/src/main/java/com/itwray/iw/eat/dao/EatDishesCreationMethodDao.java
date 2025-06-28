package com.itwray.iw.eat.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.common.constants.CommonConstants;
import com.itwray.iw.eat.mapper.EatDishesCreationMethodMapper;
import com.itwray.iw.eat.model.dto.DishesCreationMethodAddDto;
import com.itwray.iw.eat.model.entity.EatDishesCreationMethodEntity;
import com.itwray.iw.eat.model.vo.DishesCreationMethodVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 菜品制作方法表 DAO
 *
 * @author wray
 * @since 2024/5/14
 */
@Component
public class EatDishesCreationMethodDao extends ServiceImpl<EatDishesCreationMethodMapper, EatDishesCreationMethodEntity> {

    /**
     * 保存菜品制作方法
     * <p>同时删除历史制作方法</p>
     *
     * @param dishesId                 菜品id
     * @param dishesCreationMethodList 制作方法对象集合
     */
    public void saveDishesCreationMethod(Integer dishesId, List<DishesCreationMethodAddDto> dishesCreationMethodList) {
        this.lambdaUpdate().eq(EatDishesCreationMethodEntity::getDishesId, dishesId).remove();
        if (CollUtil.isNotEmpty(dishesCreationMethodList)) {
            AtomicInteger step = new AtomicInteger(1);
            List<EatDishesCreationMethodEntity> entityList = dishesCreationMethodList.stream()
                    .map(t -> {
                        EatDishesCreationMethodEntity entity = new EatDishesCreationMethodEntity();
                        entity.setStep(step.getAndIncrement());
                        entity.setDishesId(dishesId);
                        entity.setStepImage(t.getStepImage());
                        entity.setStepContent(Optional.ofNullable(t.getStepContent()).orElse(CommonConstants.EMPTY));
                        return entity;
                    }).collect(Collectors.toList());
            this.saveBatch(entityList);
        }
    }

    /**
     * 查询菜品制作方法明细
     *
     * @param dishesId 菜品id
     * @return 菜品制作方法明细
     */
    public List<DishesCreationMethodVo> getListByDishesId(Integer dishesId) {
        return this.lambdaQuery()
                .eq(EatDishesCreationMethodEntity::getDishesId, dishesId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, DishesCreationMethodVo.class))
                .collect(Collectors.toList());
    }
}
