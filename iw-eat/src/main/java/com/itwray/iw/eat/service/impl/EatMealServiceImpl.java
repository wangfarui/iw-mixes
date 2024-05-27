package com.itwray.iw.eat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.eat.dao.EatDishesMaterialDao;
import com.itwray.iw.eat.dao.EatMealDao;
import com.itwray.iw.eat.dao.EatMealMenuDao;
import com.itwray.iw.eat.model.dto.MealAddDto;
import com.itwray.iw.eat.model.dto.MealPageDto;
import com.itwray.iw.eat.model.dto.MealUpdateDto;
import com.itwray.iw.eat.model.entity.EatDishesMaterialEntity;
import com.itwray.iw.eat.model.entity.EatMealEntity;
import com.itwray.iw.eat.model.enums.MealTimeEnum;
import com.itwray.iw.eat.model.vo.MealDetailVo;
import com.itwray.iw.eat.model.vo.MealDishesMaterialDetailVo;
import com.itwray.iw.eat.model.vo.MealMenuDetailVo;
import com.itwray.iw.eat.model.vo.MealPageVo;
import com.itwray.iw.eat.service.EatMealService;
import com.itwray.iw.web.model.PageVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用餐表 服务实现类
 *
 * @author wray
 * @since 2024-04-23
 */
@Service
public class EatMealServiceImpl implements EatMealService {

    @Resource
    private EatMealDao eatMealDao;
    @Resource
    private EatMealMenuDao eatMealMenuDao;
    @Resource
    private EatDishesMaterialDao eatDishesMaterialDao;

    @Override
    @Transactional
    public Integer add(MealAddDto dto) {
        EatMealEntity eatMealEntity = BeanUtil.copyProperties(dto, EatMealEntity.class);
        eatMealDao.save(eatMealEntity);
        eatMealMenuDao.saveMealMenu(eatMealEntity.getId(), dto.getMealMenuList());
        return eatMealEntity.getId();
    }

    @Override
    @Transactional
    public void update(MealUpdateDto dto) {
        eatMealDao.queryById(dto.getId());
        eatMealDao.lambdaUpdate()
                .eq(EatMealEntity::getId, dto.getId())
                .set(EatMealEntity::getMealDate, dto.getMealDate())
                .set(EatMealEntity::getMealTime, dto.getMealTime())
                .set(EatMealEntity::getDiners, dto.getDiners())
                .set(EatMealEntity::getRemark, dto.getRemark())
                .set(EatMealEntity::getUpdateTime, LocalDateTime.now())
                .update();
        eatMealMenuDao.saveMealMenu(dto.getId(), dto.getMealMenuList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        eatMealDao.removeById(id);
    }

    @Override
    public PageVo<MealPageVo> page(MealPageDto dto) {
        LambdaQueryWrapper<EatMealEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getMealDate() != null, EatMealEntity::getMealDate, dto.getMealDate());
        queryWrapper.orderByDesc(EatMealEntity::getId);
        PageVo<MealPageVo> page = eatMealDao.page(dto, queryWrapper, MealPageVo.class);
        page.getRecords().forEach(t -> {
            t.setMealTimeDesc(MealTimeEnum.getDesc(t.getMealTime()));
        });
        return page;
    }

    @Override
    public MealDetailVo detail(Integer id) {
        EatMealEntity eatMealEntity = eatMealDao.queryById(id);
        List<MealMenuDetailVo> detailList = eatMealMenuDao.getListByMealId(id);
        MealDetailVo vo = BeanUtil.copyProperties(eatMealEntity, MealDetailVo.class);
        vo.setMealTimeDesc(MealTimeEnum.getDesc(eatMealEntity.getMealTime()));
        vo.setMealMenuList(detailList);
        return vo;
    }

    @Override
    public MealDishesMaterialDetailVo dishesMaterialDetail(Integer mealId) {
        // 查询用餐记录的菜单列表
        List<MealMenuDetailVo> detailList = eatMealMenuDao.getListByMealId(mealId);
        if (CollUtil.isEmpty(detailList)) {
            return new MealDishesMaterialDetailVo();
        }

        // 获取所有菜品id，查询菜品的食材实体对象列表
        List<Integer> dishesIdList = detailList.stream().map(MealMenuDetailVo::getDishesId).collect(Collectors.toList());
        List<EatDishesMaterialEntity> dishesMaterialList = eatDishesMaterialDao.getEntityListByDishesIds(dishesIdList);

        // 需要采购的食材对象：<食材名称, List<食材用量>>
        Map<String, List<String>> materialMap = new HashMap<>();
        // 不需要采购的食材名称
        HashSet<String> commonMaterialSet = new HashSet<>();

        for (EatDishesMaterialEntity dishesMaterial : dishesMaterialList) {
            String materialName = dishesMaterial.getMaterialName();
            if (Boolean.TRUE.equals(dishesMaterial.getIsPurchase())) {
                List<String> materialDosages = materialMap.get(materialName);
                if (materialDosages == null) {
                    materialDosages = new ArrayList<>();
                    materialDosages.add(dishesMaterial.getMaterialDosage());
                    materialMap.put(materialName, materialDosages);
                } else {
                    materialDosages.add(dishesMaterial.getMaterialDosage());
                }
            } else {
                commonMaterialSet.add(materialName);
            }
        }
        List<MealDishesMaterialDetailVo.NeedPurchaseMaterial> needPurchaseMaterialList = materialMap.entrySet().stream()
                .map(entry -> new MealDishesMaterialDetailVo.NeedPurchaseMaterial(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new MealDishesMaterialDetailVo(needPurchaseMaterialList, commonMaterialSet);
    }
}
