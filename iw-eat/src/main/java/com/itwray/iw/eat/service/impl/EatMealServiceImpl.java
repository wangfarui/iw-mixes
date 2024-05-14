package com.itwray.iw.eat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.eat.dao.EatMealDao;
import com.itwray.iw.eat.dao.EatMealMenuDao;
import com.itwray.iw.eat.model.dto.MealAddDto;
import com.itwray.iw.eat.model.dto.MealPageDto;
import com.itwray.iw.eat.model.dto.MealUpdateDto;
import com.itwray.iw.eat.model.entity.EatMealEntity;
import com.itwray.iw.eat.model.enums.MealTimeEnum;
import com.itwray.iw.eat.model.vo.MealDetailVo;
import com.itwray.iw.eat.model.vo.MealMenuDetailVo;
import com.itwray.iw.eat.model.vo.MealPageVo;
import com.itwray.iw.eat.service.EatMealService;
import com.itwray.iw.web.model.PageVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
}
