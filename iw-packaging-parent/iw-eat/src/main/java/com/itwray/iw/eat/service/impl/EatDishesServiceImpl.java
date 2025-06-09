package com.itwray.iw.eat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.eat.dao.EatDishesCreationMethodDao;
import com.itwray.iw.eat.dao.EatDishesDao;
import com.itwray.iw.eat.dao.EatDishesMaterialDao;
import com.itwray.iw.eat.model.dto.DishesAddDto;
import com.itwray.iw.eat.model.dto.DishesPageDto;
import com.itwray.iw.eat.model.dto.DishesUpdateDto;
import com.itwray.iw.eat.model.entity.EatDishesEntity;
import com.itwray.iw.eat.model.vo.DishesDetailVo;
import com.itwray.iw.eat.model.vo.DishesPageVo;
import com.itwray.iw.eat.service.EatDishesService;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.utils.UserUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private EatDishesMaterialDao eatDishesMaterialDao;
    @Resource
    private EatDishesCreationMethodDao eatDishesCreationMethodDao;

    @Override
    @Transactional
    public Integer add(DishesAddDto dto) {
        this.validDishesNameRepeat(dto.getDishesName(), null);
        EatDishesEntity eatDishesEntity = BeanUtil.copyProperties(dto, EatDishesEntity.class);
        eatDishesDao.save(eatDishesEntity);
        this.saveDishesDetail(eatDishesEntity.getId(), dto);
        return eatDishesEntity.getId();
    }

    @Override
    @Transactional
    public void update(DishesUpdateDto dto) {
        EatDishesEntity eatDishesEntity = eatDishesDao.queryById(dto.getId());
        this.validOperatorPermission(eatDishesEntity.getUserId());
        this.validDishesNameRepeat(dto.getDishesName(), dto.getId());
        eatDishesDao.lambdaUpdate()
                .eq(EatDishesEntity::getId, dto.getId())
                .set(EatDishesEntity::getDishesName, dto.getDishesName())
                .set(EatDishesEntity::getDishesImage, dto.getDishesImage())
                .set(EatDishesEntity::getDishesType, dto.getDishesType())
                .set(EatDishesEntity::getDifficultyFactor, dto.getDifficultyFactor())
                .set(EatDishesEntity::getUseTime, dto.getUseTime())
                .set(EatDishesEntity::getPrices, dto.getPrices())
                .set(EatDishesEntity::getRemark, dto.getRemark())
                .update();

        this.saveDishesDetail(dto.getId(), dto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        EatDishesEntity eatDishesEntity = eatDishesDao.queryById(id);
        this.validOperatorPermission(eatDishesEntity.getUserId());
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
        DishesDetailVo vo = BeanUtil.copyProperties(eatDishesEntity, DishesDetailVo.class);
        vo.setDishesMaterialList(eatDishesMaterialDao.getListByDishesId(id));
        vo.setDishesCreationMethodList(eatDishesCreationMethodDao.getListByDishesId(id));
        return vo;
    }

    @Override
    public List<DishesPageVo> recommendDishes() {
        EatDishesEntity entity1 = eatDishesDao.getBaseMapper().randDishes(1);
        EatDishesEntity entity2 = eatDishesDao.getBaseMapper().randDishes(2);
        EatDishesEntity entity3 = eatDishesDao.getBaseMapper().randDishes(3);
        DishesPageVo vo1 = BeanUtil.copyProperties(entity1, DishesPageVo.class);
        DishesPageVo vo2 = BeanUtil.copyProperties(entity2, DishesPageVo.class);
        DishesPageVo vo3 = BeanUtil.copyProperties(entity3, DishesPageVo.class);
        List<DishesPageVo> result = new ArrayList<>();
        if (vo1 != null) {
            result.add(vo1);
        }
        if (vo2 != null) {
            result.add(vo2);
        }
        if (vo3 != null) {
            result.add(vo3);
        }
        return result;
    }

    private void saveDishesDetail(Integer dishesId, DishesAddDto dto) {
        eatDishesMaterialDao.saveDishesMaterial(dishesId, dto.getDishesMaterialList());
        eatDishesCreationMethodDao.saveDishesCreationMethod(dishesId, dto.getDishesCreationMethodList());
    }

    /**
     * 校验菜品名称是否重复
     * <p>重复后直接抛出异常</p>
     *
     * @param dishesName 菜品名称
     * @param dishesId   编辑时的菜品id
     */
    private void validDishesNameRepeat(String dishesName, Integer dishesId) {
        EatDishesEntity entity = eatDishesDao.lambdaQuery()
                .eq(EatDishesEntity::getDishesName, dishesName)
                .ne(dishesId != null, EatDishesEntity::getId, dishesId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (entity != null) {
            throw new IwWebException("保存失败，菜品名称已存在");
        }
    }

    /**
     * 校验操作人权限
     *
     * @param hasPermissionUserId 具有操作权限的用户id
     */
    private void validOperatorPermission(Integer hasPermissionUserId) {
        Integer userId = UserUtils.getUserId();
        if (!userId.equals(hasPermissionUserId)) {
            throw new IwWebException("没有权限操作");
        }
    }
}
