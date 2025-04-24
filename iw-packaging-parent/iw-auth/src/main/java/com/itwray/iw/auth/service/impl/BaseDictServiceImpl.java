package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.DictAddDto;
import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.dto.DictUpdateDto;
import com.itwray.iw.auth.model.vo.*;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.constants.EnableEnum;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.MQTopicConstants;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.mapper.BaseDictMapper;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.model.enums.mq.RegisterNewUserTopicEnum;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
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
@Slf4j
@RocketMQMessageListener(consumerGroup = "auth-dict-service", topic = MQTopicConstants.REGISTER_NEW_USER, tag = "init")
public class BaseDictServiceImpl extends WebServiceImpl<BaseDictDao, BaseDictMapper, BaseDictEntity,
        DictAddDto, DictUpdateDto, DictDetailVo, Integer> implements BaseDictService, RocketMQClientListener<UserAddBo> {

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
                .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
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
                .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
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
        RedisUtil.expire(this.obtainDictRedisKeyByUser(), AuthRedisKeyEnum.DICT_KEY.getExpireTime());

        return dictMap;
    }

    @Override
    @Transactional
    public Integer add(DictAddDto dto) {
        this.checkSaveParam(dto);

        // 如果新增时没有指定sort值
        if (NumberUtils.isNullOrZero(dto.getSort())) {
            // 根据字典类型查询当前最大sort值
            dto.setSort(getBaseDao().queryNextSortValue(dto.getDictType()));
        }

        Integer id = super.add(dto);

        // 更新Redis缓存
        List<DictAllListVo> dictAllListVos = queryAllDictByType(dto.getDictType());
        RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), dto.getDictType(), dictAllListVos);
        RedisUtil.expire(this.obtainDictRedisKeyByUser(), AuthRedisKeyEnum.DICT_KEY.getExpireTime());

        return id;
    }

    @Override
    @Transactional
    public void update(DictUpdateDto dto) {
        this.checkSaveParam(dto);

        // 根据id查询字典类型
        BaseDictEntity baseDictEntity = this.checkDataSecurity(dto.getId(), dto.getDictStatus());

        super.update(dto);

        // 更新Redis缓存
        List<DictAllListVo> dictAllListVos = queryAllDictByType(baseDictEntity.getDictType());
        RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), baseDictEntity.getDictType(), dictAllListVos);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 根据id查询字典类型
        BaseDictEntity dictEntity = this.checkDataSecurity(id, null);

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
                .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
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
    private BaseDictEntity checkDataSecurity(Serializable id, Integer dictStatus) {
        BaseDictEntity dictEntity = getBaseDao().queryById(id);

        // 判断当前字典类型是否少于1个值
        Long dictEntityCounts = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictEntity.getDictType())
                .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
                .ne(dictStatus == null || BoolEnum.FALSE.getCode().equals(dictStatus), BaseDictEntity::getId, id)
                .count();
        if (dictEntityCounts < 1) {
            throw new BusinessException("当前字典类型至少需要包含一个启用的字典值！");
        }

        return dictEntity;
    }

    /**
     * 检测保存时的参数合法性
     */
    private void checkSaveParam(DictAddDto dto) {
        DictTypeEnum dictTypeEnum = ConstantEnumUtil.findByType(DictTypeEnum.class, dto.getDictType());
        if (dictTypeEnum == null) {
            throw new BusinessException("字典类型错误");
        }
        if (dictTypeEnum.getDataType().equals(DictTypeEnum.DataType.CODE) && dto.getDictCode() == null) {
            throw new BusinessException("CODE类型的字典项, 其字典code不能为空");
        }
    }

    /**
     * 通过用户获取字典redisKey
     *
     * @return dict:[userId]
     */
    private String obtainDictRedisKeyByUser() {
        return AuthRedisKeyEnum.DICT_KEY.getKey(UserUtils.getUserId());
    }

    @Override
    public Class<UserAddBo> getGenericClass() {
        return UserAddBo.class;
    }

    /**
     * 新用户注册初始化基础字典数据
     *
     * @param bo 新用户对象
     */
    @Override
    @Transactional
    public void doConsume(UserAddBo bo) {
        // 初始化字典数据
        this.initDictData(bo);

        // 字典数据初始化完成之后, 发送 依赖字典数据 的消息
        MQProducerHelper.send(RegisterNewUserTopicEnum.DEPEND_DICT, bo);
    }

    /**
     * 新用户注册初始化基础字典数据
     */
    private void initDictData(UserAddBo bo) {
        // 判断该用户是否已生成过字典数据
        BaseDictEntity dictEntity = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getUserId, bo.getUserId())
                .select(BaseDictEntity::getId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (dictEntity != null) {
            log.info("用户[{}]已存在基础字典数据, 默认跳过初始化基础字典数据操作", bo.getUserId());
            return;
        }

        // 查询父字典数据
        List<BaseDictEntity> parentTemplateDictList = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getUserId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                .eq(BaseDictEntity::getParentId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
                .list();
        if (CollectionUtil.isEmpty(parentTemplateDictList)) {
            log.info("数据库内无模板字典数据, 用户[{}]默认跳过初始化基础字典数据操作", bo.getUserId());
            return;
        }
        for (BaseDictEntity parentDict : parentTemplateDictList) {
            // 保存父字典到数据库
            BaseDictEntity newParentDict = BeanUtil.copyProperties(parentDict, BaseDictEntity.class);
            newParentDict.setId(null);  // id重置为空，采用数据库自增id
            newParentDict.setUserId(bo.getUserId()); // 使用新用户id
            getBaseDao().save(newParentDict);

            // 通过父字典查询子字典数据
            List<BaseDictEntity> sonTemplateDictList = getBaseDao().lambdaQuery()
                    .eq(BaseDictEntity::getUserId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                    .eq(BaseDictEntity::getParentId, parentDict.getId())
                    .eq(BaseDictEntity::getDictStatus, EnableEnum.ENABLE.getCode())
                    .list();
            if (CollectionUtil.isEmpty(sonTemplateDictList)) {
                continue;
            }
            // 保存子字典数据到数据库
            sonTemplateDictList.forEach(entity -> {
                entity.setId(null);
                entity.setParentId(newParentDict.getId());
                entity.setUserId(bo.getUserId());
            });
            getBaseDao().saveBatch(sonTemplateDictList);
        }

        // 移除用户业务字典的缓存
        RedisUtil.delete(this.obtainDictRedisKeyByUser());

        log.info("用户[{}]基础字典数据初始化完成", bo.getUserId());
    }
}
