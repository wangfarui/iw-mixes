package com.itwray.iw.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.eat.dao.EatFridgeFoodDao;
import com.itwray.iw.eat.mapper.EatFridgeFoodMapper;
import com.itwray.iw.eat.model.dto.EatFridgeFoodAddDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodPageDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodUpdateDto;
import com.itwray.iw.eat.model.entity.EatFridgeFoodEntity;
import com.itwray.iw.eat.model.vo.EatFridgeFoodDetailVo;
import com.itwray.iw.eat.model.vo.EatFridgeFoodPageVo;
import com.itwray.iw.eat.service.EatFridgeFoodService;
import com.itwray.iw.web.model.enums.SortTypeEnum;
import com.itwray.iw.web.model.enums.SortWayEnum;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 冰箱食材表 服务实现类
 *
 * @author wray
 * @since 2026-01-20
 */
@Service
public class EatFridgeFoodServiceImpl extends WebServiceImpl<EatFridgeFoodDao, EatFridgeFoodMapper, EatFridgeFoodEntity,
        EatFridgeFoodAddDto, EatFridgeFoodUpdateDto, EatFridgeFoodDetailVo, Integer> implements EatFridgeFoodService {

    @Autowired
    public EatFridgeFoodServiceImpl(EatFridgeFoodDao baseDao) {
        super(baseDao);
    }

    @Override
    public PageVo<EatFridgeFoodPageVo> page(EatFridgeFoodPageDto dto) {
        LambdaQueryWrapper<EatFridgeFoodEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), EatFridgeFoodEntity::getName, dto.getName())
                .eq(Objects.nonNull(dto.getCategory()), EatFridgeFoodEntity::getCategory, dto.getCategory())
                .eq(Objects.nonNull(dto.getSection()), EatFridgeFoodEntity::getSection, dto.getSection())
                .ge(Objects.nonNull(dto.getExpireStartDate()), EatFridgeFoodEntity::getExpireDate, dto.getExpireStartDate())
                .le(Objects.nonNull(dto.getExpireEndDate()), EatFridgeFoodEntity::getExpireDate, dto.getExpireEndDate())
                .orderByAsc(SortWayEnum.isAsc(dto.getSortWay()), SortTypeEnum.getDefaultSortField(dto.getSortType()))
                .orderByDesc(!SortWayEnum.isAsc(dto.getSortWay()), SortTypeEnum.getDefaultSortField(dto.getSortType()));
        return getBaseDao().page(dto, queryWrapper, EatFridgeFoodPageVo.class);
    }

    @Override
    public void markAsUsed(Integer id) {
        getBaseDao().removeById(id);
    }
}
