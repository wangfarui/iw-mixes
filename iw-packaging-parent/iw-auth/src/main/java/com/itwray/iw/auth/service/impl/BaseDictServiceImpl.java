package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.model.RedisKeyConstants;
import com.itwray.iw.auth.model.dto.DictAddDto;
import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.vo.*;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.common.constants.EnableEnums;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.mapper.BaseDictMapper;
import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.dto.UpdateDto;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public List<DictListVo> getDictList(Integer dictType) {
        return getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictType)
                .eq(BaseDictEntity::getDictStatus, EnableEnums.ENABLE.getCode())
                .orderByAsc(BaseDictEntity::getSort)
                .list()
                .stream().map(t -> BeanUtil.copyProperties(t, DictListVo.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DictAllListVo>> getAllDictList(Boolean latest) {
        // 默认不查询最新实时字典数据
        if (latest == null || !latest) {
            Map<Object, Object> dictMapCache = RedisUtil.getHashEntries(this.obtainDictRedisKeyByUser());
            if (!dictMapCache.isEmpty()) {
                return (Map) dictMapCache;
            }
        }

        Map<String, List<DictAllListVo>> dictMap = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictStatus, EnableEnums.ENABLE.getCode())
                .list()
                .stream()
                .collect(Collectors.groupingBy(t -> t.getDictType().toString(),
                        // 对每个分组先排序，再进行转换
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                (List<BaseDictEntity> list) -> {
                                    // 对每个分组的 List<BaseDictEntity> 按 sort 字段排序
                                    list.sort(Comparator.comparing(BaseDictEntity::getSort));

                                    // 将排序后的 BaseDictEntity 转换为 DictAllListVo
                                    return list.stream()
                                            .map(baseDictEntity -> new DictAllListVo(
                                                    baseDictEntity.getId(),
                                                    baseDictEntity.getDictCode(),
                                                    baseDictEntity.getDictName())
                                            )
                                            .collect(Collectors.toList());
                                }
                        )));

        RedisUtil.add(this.obtainDictRedisKeyByUser(), dictMap);

        return dictMap;
    }

    @Override
    @Transactional
    public Serializable add(AddDto dto) {
        if (dto instanceof DictAddDto dictAddDto) {
            // 如果新增时没有指定sort值
            if (NumberUtils.isNullOrZero(dictAddDto.getSort())) {
                // 根据字典类型查询当前最大sort值
                dictAddDto.setSort(getBaseDao().queryNextSortValue(dictAddDto.getDictType()));
            }
        }

        Serializable id = super.add(dto);

        if (dto instanceof DictAddDto dictAddDto) {
            // 更新Redis缓存
            List<DictAllListVo> dictAllListVos = queryAllDictByType(dictAddDto.getDictType());
            RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), dictAddDto.getDictType(), dictAllListVos);
        }

        return id;
    }

    @Override
    @Transactional
    public void update(UpdateDto dto) {
        // 根据id查询字典类型
        BaseDictEntity baseDictEntity = this.checkDataSecurity(dto.getId());

        super.update(dto);

        // 更新Redis缓存
        List<DictAllListVo> dictAllListVos = queryAllDictByType(baseDictEntity.getDictType());
        RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), baseDictEntity.getDictType(), dictAllListVos);
    }

    @Override
    @Transactional
    public void delete(Serializable id) {
        // 根据id查询字典类型
        BaseDictEntity dictEntity = this.checkDataSecurity(id);

        super.delete(id);

        // 更新Redis缓存
        List<DictAllListVo> dictAllListVos = queryAllDictByType(dictEntity.getDictType());
        RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), dictEntity.getDictType(), dictAllListVos);
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

    private List<DictAllListVo> queryAllDictByType(Integer dictType) {
        return getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictType)
                .eq(BaseDictEntity::getDictStatus, EnableEnums.ENABLE.getCode())
                .orderByAsc(BaseDictEntity::getSort)
                .list()
                .stream()
                .map(t -> new DictAllListVo(t.getId(), t.getDictCode(), t.getDictName()))
                .collect(Collectors.toList());
    }

    /**
     * 校验数据安全
     *
     * @param id 字典id
     * @return BaseDictEntity
     */
    private BaseDictEntity checkDataSecurity(Serializable id) {
        BaseDictEntity dictEntity = getBaseDao().queryById(id);

        // 判断当前字典类型是否少于1个值
        Long dictEntityCounts = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictEntity.getDictType())
                .eq(BaseDictEntity::getDictStatus, EnableEnums.ENABLE.getCode())
                .count();
        if (dictEntityCounts <= 1) {
            throw new IwWebException("当前字典类型至少需要包含一个启用的字典值！");
        }

        return dictEntity;
    }

    /**
     * 通过用户获取字典redisKey
     *
     * @return dict:[userId]
     */
    private String obtainDictRedisKeyByUser() {
        return RedisKeyConstants.DICT_KEY + UserUtils.getUserId();
    }
}
