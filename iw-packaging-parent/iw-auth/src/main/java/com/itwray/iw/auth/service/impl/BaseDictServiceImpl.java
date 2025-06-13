package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.auth.model.dto.DictAddDto;
import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.dto.DictUpdateDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.vo.*;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.constants.EnableEnum;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.starter.redis.lock.RedisLockUtil;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.mapper.BaseDictMapper;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.model.enums.RoleTypeEnum;
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
@RocketMQMessageListener(consumerGroup = "auth-dict-service", topic = RegisterNewUserTopicEnum.TOPIC, tag = "init")
public class BaseDictServiceImpl extends WebServiceImpl<BaseDictDao, BaseDictMapper, BaseDictEntity,
        DictAddDto, DictUpdateDto, DictDetailVo, Integer> implements BaseDictService, RocketMQClientListener<UserAddBo> {

    private AuthUserDao authUserDao;

    /**
     * 操作管理员字典 分布式锁Key
     * <p>全局锁</p>
     */
    private static final String OPERATE_ADMIN_DICT_LOCK_KEY = "OperateAdminDict";

    @Autowired
    public BaseDictServiceImpl(BaseDictDao baseDao) {
        super(baseDao);
    }

    @Autowired
    public void setAuthUserDao(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Override
    public List<DictTypeVo> getDictTypeList() {
        boolean adminUser = this.isAdminUser(UserUtils.getUserId());
        return Arrays.stream(DictTypeEnum.values())
                // 如果是管理员用户, 则默认所有字典为true. 如果不是管理员用户, 则只返回非管理员字典
                .filter(t -> !t.isAdminDict() || adminUser)
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
        boolean isAdminDict = this.verifyUserPermission(dto.getDictType());
        this.checkSaveParam(dto);

        // 如果新增时没有指定sort值
        if (NumberUtils.isNullOrZero(dto.getSort())) {
            // 根据字典类型查询当前最大sort值
            dto.setSort(getBaseDao().queryNextSortValue(dto.getDictType()));
        }

        // 如果是管理员字典项, 则需要同步给所有用户
        if (isAdminDict) {
            RedisLockUtil.lock(OPERATE_ADMIN_DICT_LOCK_KEY);
            try {
                List<AuthUserEntity> userEntityList = authUserDao.getBaseMapper().queryAllUser();
                BaseDictEntity dictEntity = BeanUtil.copyProperties(dto, BaseDictEntity.class);
                for (AuthUserEntity userEntity : userEntityList) {
                    dictEntity.setId(null);
                    dictEntity.setUserId(userEntity.getId());
                    getBaseDao().save(dictEntity);
                    // 删除其Redis缓存
                    RedisUtil.delete(this.obtainDictRedisKeyByUser(userEntity.getId()));
                }
                // 同步所有用户字典后, 返回的字典id默认为0
                return 0;
            } finally {
                RedisLockUtil.unlock(OPERATE_ADMIN_DICT_LOCK_KEY);
            }
        } else {
            Integer id = super.add(dto);
            // 更新Redis缓存
            List<DictAllListVo> dictAllListVos = queryAllDictByType(dto.getDictType());
            RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), dto.getDictType(), dictAllListVos);
            RedisUtil.expire(this.obtainDictRedisKeyByUser(), AuthRedisKeyEnum.DICT_KEY.getExpireTime());
            return id;
        }
    }

    @Override
    @Transactional
    public void update(DictUpdateDto dto) {
        boolean isAdminDict = this.verifyUserPermission(dto.getDictType());
        this.checkSaveParam(dto);

        // 根据id查询字典类型
        BaseDictEntity baseDictEntity = this.checkDataSecurity(dto.getId(), dto.getDictStatus());

        // 如果是管理员字典项, 则需要同步给所有用户
        if (isAdminDict) {
            BaseDictEntity updateEntity = new BaseDictEntity();
            updateEntity.setDictCode(dto.getDictCode());
            updateEntity.setDictName(dto.getDictName());
            updateEntity.setDictStatus(dto.getDictStatus());
            updateEntity.setSort(dto.getSort());
            RedisLockUtil.lock(OPERATE_ADMIN_DICT_LOCK_KEY);
            try {
                getBaseDao().getBaseMapper().updateAllDictByDictName(baseDictEntity.getDictType(), baseDictEntity.getDictName(), updateEntity);
                // 查询所有用户
                List<AuthUserEntity> userEntityList = authUserDao.getBaseMapper().queryAllUser();
                for (AuthUserEntity userEntity : userEntityList) {
                    // 删除其Redis缓存
                    RedisUtil.delete(this.obtainDictRedisKeyByUser(userEntity.getId()));
                }
            } finally {
                RedisLockUtil.unlock(OPERATE_ADMIN_DICT_LOCK_KEY);
            }
        } else {
            super.update(dto);
            // 更新Redis缓存
            List<DictAllListVo> dictAllListVos = queryAllDictByType(baseDictEntity.getDictType());
            RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), baseDictEntity.getDictType(), dictAllListVos);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        BaseDictEntity dictEntity = getBaseDao().queryById(id);
        boolean isAdminDict = this.verifyUserPermission(dictEntity.getDictType());

        // 根据id查询字典类型
        this.checkDataSecurity(id, null);

        // 如果是管理员字典项, 则需要同步给所有用户
        if (isAdminDict) {
            BaseDictEntity updateEntity = new BaseDictEntity();
            updateEntity.setDeleted(Boolean.TRUE);
            RedisLockUtil.lock(OPERATE_ADMIN_DICT_LOCK_KEY);
            try {
                getBaseDao().getBaseMapper().updateAllDictByDictName(dictEntity.getDictType(), dictEntity.getDictName(), updateEntity);
                // 查询所有用户
                List<AuthUserEntity> userEntityList = authUserDao.getBaseMapper().queryAllUser();
                for (AuthUserEntity userEntity : userEntityList) {
                    // 删除其Redis缓存
                    RedisUtil.delete(this.obtainDictRedisKeyByUser(userEntity.getId()));
                }
            } finally {
                RedisLockUtil.unlock(OPERATE_ADMIN_DICT_LOCK_KEY);
            }
        } else {
            super.delete(id);
            // 更新Redis缓存
            List<DictAllListVo> dictAllListVos = queryAllDictByType(dictEntity.getDictType());
            RedisUtil.putHashKey(this.obtainDictRedisKeyByUser(), dictEntity.getDictType(), dictAllListVos);
        }
    }

    @Override
    public PageVo<DictPageVo> page(DictPageDto dto) {
        LambdaQueryWrapper<BaseDictEntity> queryWrapper = new LambdaQueryWrapper<>();
        boolean adminUser = this.isAdminUser(UserUtils.getUserId());
        queryWrapper.eq(dto.getDictType() != null, BaseDictEntity::getDictType, dto.getDictType())
                .eq(dto.getDictCode() != null, BaseDictEntity::getDictCode, dto.getDictCode())
                .eq(dto.getDictStatus() != null, BaseDictEntity::getDictStatus, dto.getDictStatus())
                .in(!adminUser, BaseDictEntity::getDictType, DictTypeEnum.getUserDict().stream().map(DictTypeEnum::getCode).toList())
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

        Integer oldDictId = null;
        // 如果是更新操作
        if (dto instanceof DictUpdateDto updateDto) {
            oldDictId = updateDto.getId();
            // 查询历史字典
            BaseDictEntity oldDictEntity = getBaseDao().queryById(oldDictId);
            DictTypeEnum oldDictTypeEnum = ConstantEnumUtil.findByType(DictTypeEnum.class, oldDictEntity.getDictType());
            // 如果修改的是管理员字典, 则不能修改其 dictType 值
            if (oldDictTypeEnum.isAdminDict() && !dictTypeEnum.equals(oldDictTypeEnum)) {
                throw new BusinessException("管理员字典不能修改其字典类型");
            }
            // 如果修改的不是管理员字典, 而不能将其修改为管理员字典
            if (!oldDictTypeEnum.isAdminDict() && dictTypeEnum.isAdminDict()) {
                throw new BusinessException("普通字典不能修改为管理员字典");
            }
        }
        if (dictTypeEnum.getDataType().equals(DictTypeEnum.DataType.CODE)) {
            if (dto.getDictCode() == null) {
                throw new BusinessException("CODE类型的字典项, 其字典code不能为空");
            }
            // 检测字典code是否重复
            Long count = getBaseDao().lambdaQuery()
                    .eq(BaseDictEntity::getDictType, dto.getDictType())
                    .eq(BaseDictEntity::getDictCode, dto.getDictCode())
                    .ne(oldDictId != null, BaseDictEntity::getId, oldDictId)
                    .count();
            if (count > 0) {
                throw new BusinessException("CODE类型的字典项, 其字典code不能重复");
            }
        }
        // 检测字典name是否重复
        Long count = getBaseDao().lambdaQuery()
                .eq(BaseDictEntity::getDictType, dto.getDictType())
                .eq(BaseDictEntity::getDictName, dto.getDictName())
                .ne(oldDictId != null, BaseDictEntity::getId, oldDictId)
                .count();
        if (count > 0) {
            throw new BusinessException("字典名称不能重复");
        }
    }

    /**
     * 校验用户操作字典的角色权限
     *
     * @param dictType 字典类型code
     * @return 是否为管理员字典项 true->是
     */
    private boolean verifyUserPermission(Integer dictType) {
        DictTypeEnum dictTypeEnum = ConstantEnumUtil.findByType(DictTypeEnum.class, dictType);
        if (dictTypeEnum == null) {
            throw new BusinessException("无权操作");
        }
        if (dictTypeEnum.isAdminDict()) {
            if (!this.isAdminUser(UserUtils.getUserId())) {
                throw new BusinessException("无权操作");
            }
        }
        return dictTypeEnum.isAdminDict();
    }

    /**
     * 是否为管理员用户
     *
     * @param userId 用户id
     * @return true -> 是
     */
    private boolean isAdminUser(Integer userId) {
        AuthUserEntity authUserEntity = authUserDao.queryById(userId);
        return RoleTypeEnum.isAdminRole(authUserEntity.getRoleType());
    }

    /**
     * 通过用户获取字典redisKey
     *
     * @return dict:[userId]
     */
    private String obtainDictRedisKeyByUser() {
        return obtainDictRedisKeyByUser(UserUtils.getUserId());
    }

    /**
     * 通过用户获取字典redisKey
     *
     * @param userId 指定用户id
     * @return dict:[userId]
     */
    private String obtainDictRedisKeyByUser(Integer userId) {
        return AuthRedisKeyEnum.DICT_KEY.getKey(userId);
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

        RedisLockUtil.lock(OPERATE_ADMIN_DICT_LOCK_KEY);
        try {
            // 查询父字典数据(仅角色类型为用户类型的字典)
            List<BaseDictEntity> parentTemplateDictList = getBaseDao().lambdaQuery()
                    .eq(BaseDictEntity::getUserId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                    .eq(BaseDictEntity::getParentId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                    .in(BaseDictEntity::getDictType, DictTypeEnum.getUserDict().stream().map(DictTypeEnum::getCode).toList())
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
        } finally {
            RedisLockUtil.unlock(OPERATE_ADMIN_DICT_LOCK_KEY);
        }

        log.info("用户[{}]基础字典数据初始化完成", bo.getUserId());
    }
}
