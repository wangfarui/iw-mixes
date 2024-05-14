package com.itwray.iw.eat.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.eat.mapper.EatDishesMaterialMapper;
import com.itwray.iw.eat.model.dto.DishesMaterialAddDto;
import com.itwray.iw.eat.model.entity.EatDishesMaterialEntity;
import com.itwray.iw.eat.model.vo.DishesMaterialVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品用料表 DAO
 *
 * @author wray
 * @since 2024-04-23
 */
@Component
public class EatDishesMaterialDao extends ServiceImpl<EatDishesMaterialMapper, EatDishesMaterialEntity> {

    /**
     * 保存菜品用料
     *
     * @param dishesId           菜品id
     * @param dishesMaterialList 菜品用料对象集合
     */
    public void saveDishesMaterial(Integer dishesId, List<DishesMaterialAddDto> dishesMaterialList) {
        this.lambdaUpdate().eq(EatDishesMaterialEntity::getDishesId, dishesId).remove();
        if (CollUtil.isNotEmpty(dishesMaterialList)) {
            List<EatDishesMaterialEntity> entityList = dishesMaterialList.stream()
                    .map(t -> {
                        EatDishesMaterialEntity entity = BeanUtil.copyProperties(t, EatDishesMaterialEntity.class);
                        entity.setDishesId(dishesId);
                        return entity;
                    }).collect(Collectors.toList());
            this.saveBatch(entityList);
        }
    }


    /**
     * 查询菜品用料明细
     *
     * @param dishesId 菜品id
     * @return 菜品用料明细
     */
    public List<DishesMaterialVo> getListByDishesId(Integer dishesId) {
        return this.lambdaQuery()
                .eq(EatDishesMaterialEntity::getDishesId, dishesId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, DishesMaterialVo.class))
                .collect(Collectors.toList());
    }
}
