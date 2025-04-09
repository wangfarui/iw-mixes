package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.bookkeeping.dao.BookkeepingActionsDao;
import com.itwray.iw.bookkeeping.mapper.BookkeepingActionsMapper;
import com.itwray.iw.bookkeeping.model.BookkeepingRedisKeyEnum;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingActionsUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingActionsEntity;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingActionsDetailVo;
import com.itwray.iw.bookkeeping.service.BookkeepingActionsService;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.model.enums.mq.RegisterNewUserTopicEnum;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账行为表 服务实现类
 *
 * @author wray
 * @since 2025-04-08
 */
@Service
@RocketMQMessageListener(consumerGroup = "bookkeeping-service", topic = RegisterNewUserTopicEnum.TOPIC_NAME, tag = "dependDict")
public class BookkeepingActionsServiceImpl extends WebServiceImpl<BookkeepingActionsDao, BookkeepingActionsMapper, BookkeepingActionsEntity,
        BookkeepingActionsAddDto, BookkeepingActionsUpdateDto, BookkeepingActionsDetailVo, Integer> implements BookkeepingActionsService, RocketMQClientListener<UserAddBo> {

    @Autowired
    public BookkeepingActionsServiceImpl(BookkeepingActionsDao baseDao) {
        super(baseDao);
    }

    @Override
    @Transactional
    public Integer add(BookkeepingActionsAddDto dto) {
        // 查询当前用户记账行为中排序最大值
        BookkeepingActionsEntity entity = getBaseDao().lambdaQuery()
                .eq(BookkeepingActionsEntity::getUserId, UserUtils.getUserId())
                .orderByDesc(BookkeepingActionsEntity::getSort)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (entity != null) {
            dto.setSort(entity.getSort().add(BigDecimal.TEN));
        } else {
            dto.setSort(BigDecimal.TEN);
        }

        Integer id = super.add(dto);
        BookkeepingRedisKeyEnum.ACTION_LIST.delete(UserUtils.getUserId(), dto.getRecordCategory());
        return id;
    }

    @Override
    @Transactional
    public void update(BookkeepingActionsUpdateDto dto) {
        BookkeepingRedisKeyEnum.ACTION_LIST.delete(UserUtils.getUserId(), dto.getRecordCategory());
        super.update(dto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        BookkeepingActionsEntity entity = getBaseDao().queryById(id);
        BookkeepingRedisKeyEnum.ACTION_LIST.delete(UserUtils.getUserId(), entity.getRecordCategory());
        super.delete(id);
    }

    @Override
    public List<BookkeepingActionsDetailVo> list(Integer recordCategory) {
        Integer userId = UserUtils.getUserId();

        @SuppressWarnings("unchecked")
        List<BookkeepingActionsDetailVo> cache = BookkeepingRedisKeyEnum.ACTION_LIST.getStringValue(List.class, userId, recordCategory);
        if (cache != null) {
            return cache;
        }

        List<BookkeepingActionsDetailVo> voList = getBaseDao().lambdaQuery()
                .eq(BookkeepingActionsEntity::getUserId, userId)
                .eq(BookkeepingActionsEntity::getRecordCategory, recordCategory)
                .orderByAsc(BookkeepingActionsEntity::getSort)
                .orderByAsc(BookkeepingActionsEntity::getId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, BookkeepingActionsDetailVo.class))
                .toList();

        BookkeepingRedisKeyEnum.ACTION_LIST.setStringValue(voList, userId, recordCategory);

        return voList;
    }

    @Override
    public Class<UserAddBo> getGenericClass() {
        return UserAddBo.class;
    }

    @Override
    @Transactional
    public void doConsume(UserAddBo bo) {
        // 判断该用户是否已生成过字典数据
        BookkeepingActionsEntity actionsEntity = getBaseDao().lambdaQuery()
                .eq(BookkeepingActionsEntity::getUserId, bo.getUserId())
                .select(BookkeepingActionsEntity::getId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (actionsEntity != null) {
            log.info("用户[{}]已存在记账行为数据, 默认跳过初始化默认记账行为数据操作", bo.getUserId());
            return;
        }

        // 查询默认记账行为数据
        List<BookkeepingActionsEntity> defaultActionList = getBaseDao().lambdaQuery()
                .eq(BookkeepingActionsEntity::getUserId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                .list();
        if (CollectionUtil.isEmpty(defaultActionList)) {
            log.info("数据库内无默认记账行为数据, 用户[{}]默认跳过初始化默认记账行为数据操作", bo.getUserId());
            return;
        }

        // 保存默认记账行为数据
        for (BookkeepingActionsEntity action : defaultActionList) {
            BookkeepingActionsEntity newAction = BeanUtil.copyProperties(action, BookkeepingActionsEntity.class);
            newAction.setId(null);  // id重置为空，采用数据库自增id
            newAction.setUserId(bo.getUserId()); // 使用新用户id
            getBaseDao().save(newAction);
        }

        log.info("用户[{}]默认记账行为数据初始化完成", bo.getUserId());
    }
}
