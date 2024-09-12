package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.model.dto.DictAddDto;
import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.vo.BaseDictVo;
import com.itwray.iw.auth.model.vo.DictDetailVo;
import com.itwray.iw.auth.model.vo.DictPageVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.mapper.BaseDictMapper;
import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典服务实现层
 *
 * @author wray
 * @since 2024/5/26
 */
@Service
public class BaseDictServiceImpl extends WebServiceImpl<BaseDictMapper, BaseDictEntity, BaseDictDao, DictDetailVo> implements BaseDictService {

    @Autowired
    public BaseDictServiceImpl(BaseDictDao baseDao) {
        super(baseDao);
    }

    @Override
    public List<DictTypeVo> getDictTypeList() {
        return Arrays.stream(DictTypeEnum.values())
                .map(t -> new DictTypeVo(t.getCode(), t.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseDictVo> getDictList(Integer dictType) {
        return getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictType)
                .orderByAsc(BaseDictEntity::getSort)
                .list()
                .stream().map(t -> BeanUtil.copyProperties(t, BaseDictVo.class))
                .collect(Collectors.toList());
    }

    @Override
    public Serializable add(AddDto dto) {
        if (dto instanceof DictAddDto dictAddDto) {
            // 如果新增时没有指定sort值
            if (NumberUtils.isNullOrZero(dictAddDto.getSort())) {
                // 根据字典类型查询当前最大sort值
                dictAddDto.setSort(getBaseDao().queryNextSortValue(dictAddDto.getDictType()));
            }
        }

        return super.add(dto);
    }

    @Override
    public PageVo<DictPageVo> page(DictPageDto dto) {
        LambdaQueryWrapper<BaseDictEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getDictType() != null, BaseDictEntity::getDictType, dto.getDictType())
                .eq(dto.getDictCode() != null, BaseDictEntity::getDictCode, dto.getDictCode())
                .eq(dto.getDictStatus() != null, BaseDictEntity::getDictStatus, dto.getDictStatus())
                .like(dto.getDictName() != null, BaseDictEntity::getDictName, dto.getDictName());
        if (dto.getDictType() != null) {
            queryWrapper.orderByAsc(BaseDictEntity::getSort);
        } else {
            queryWrapper.orderByDesc(BaseDictEntity::getId);
        }
        return getBaseDao().page(dto, queryWrapper, DictPageVo.class);
    }
}
