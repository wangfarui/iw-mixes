package com.itwray.iw.eat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.eat.dao.EatDishesDao;
import com.itwray.iw.eat.model.dto.DishesAddDto;
import com.itwray.iw.eat.model.dto.DishesPageDto;
import com.itwray.iw.eat.model.dto.DishesUpdateDto;
import com.itwray.iw.eat.model.entity.EatDishesEntity;
import com.itwray.iw.eat.model.vo.DishesDetailVo;
import com.itwray.iw.eat.model.vo.DishesPageVo;
import com.itwray.iw.eat.service.EatDishesService;
import com.itwray.iw.web.model.PageVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜品表 服务实现类
 *
 * @author wray
 * @since 2024-04-23
 */
@Service
public class EatDishesServiceImpl implements EatDishesService {

    @Resource
    private EatDishesDao eatDishesDao;

    @Override
    @Transactional
    public Integer add(DishesAddDto dto) {
        EatDishesEntity eatDishesEntity = BeanUtil.copyProperties(dto, EatDishesEntity.class);
        eatDishesDao.save(eatDishesEntity);
        return eatDishesEntity.getId();
    }

    @Override
    @Transactional
    public void update(DishesUpdateDto dto) {
        eatDishesDao.queryById(dto.getId());
        eatDishesDao.lambdaUpdate()
                .eq(EatDishesEntity::getId, dto.getId())
                .set(EatDishesEntity::getDishesName, dto.getDishesName())
                .set(EatDishesEntity::getDishesType, dto.getDishesType())
                .set(EatDishesEntity::getDifficultyFactor, dto.getDifficultyFactor())
                .set(EatDishesEntity::getUseTime, dto.getUseTime())
                .set(EatDishesEntity::getPrices, dto.getPrices())
                .set(EatDishesEntity::getRemark, dto.getRemark())
                .update();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        eatDishesDao.removeById(id);
    }

    @Override
    public PageVo<DishesPageVo> page(DishesPageDto dto) {
        LambdaQueryWrapper<EatDishesEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getDishesName()), EatDishesEntity::getDishesName, dto.getDishesName())
                .eq(NumberUtils.isNotZero(dto.getDishesType()), EatDishesEntity::getDishesType, dto.getDishesType())
                .eq(dto.getStatus() != null, EatDishesEntity::getStatus, dto.getStatus())
                .orderByDesc(EatDishesEntity::getId);
        return eatDishesDao.page(dto, queryWrapper, DishesPageVo.class);
    }

    @Override
    public DishesDetailVo detail(Integer id) {
        EatDishesEntity eatDishesEntity = eatDishesDao.queryById(id);
        return BeanUtil.copyProperties(eatDishesEntity, DishesDetailVo.class);
    }
}
