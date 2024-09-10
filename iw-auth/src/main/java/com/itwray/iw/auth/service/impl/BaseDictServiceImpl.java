package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.auth.model.vo.BaseDictVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
public class BaseDictServiceImpl implements BaseDictService {

    @Resource
    private BaseDictDao baseDictDao;

    @Override
    public List<DictTypeVo> getDictTypeList() {
        return Arrays.stream(DictTypeEnum.values())
                .map(t -> new DictTypeVo(t.getCode(), t.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseDictVo> getDictList(Integer dictType) {
        return baseDictDao.lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictType)
                .orderByAsc(BaseDictEntity::getSort)
                .list()
                .stream().map(t -> BeanUtil.copyProperties(t, BaseDictVo.class))
                .collect(Collectors.toList());
    }
}
