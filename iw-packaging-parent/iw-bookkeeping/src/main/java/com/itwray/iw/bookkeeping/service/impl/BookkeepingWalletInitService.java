package com.itwray.iw.bookkeeping.service.impl;

import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.bookkeeping.dao.BookkeepingWalletDao;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletEntity;
import com.itwray.iw.starter.redis.lock.RedisLockUtil;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.model.enums.mq.BookkeepingRecordsTopicEnum;
import com.itwray.iw.web.model.enums.mq.RegisterNewUserTopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 记录钱包 用户初始化服务
 *
 * @author farui.wang
 * @since 2025/5/26
 */
@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = "bookkeeping-wallet-service", topic = RegisterNewUserTopicEnum.TOPIC, tag = "init")
public class BookkeepingWalletInitService implements RocketMQClientListener<UserAddBo> {

    private final BookkeepingWalletDao bookkeepingWalletDao;

    @Autowired
    public BookkeepingWalletInitService(BookkeepingWalletDao bookkeepingWalletDao) {
        this.bookkeepingWalletDao = bookkeepingWalletDao;
    }

    @Override
    public Class<UserAddBo> getGenericClass() {
        return UserAddBo.class;
    }

    @Override
    @Transactional
    public void doConsume(UserAddBo userAddBo) {
        String lockKey = BookkeepingRecordsTopicEnum.WALLET_AMOUNT.getDestination() + ":" + userAddBo.getUserId();
        RedisLockUtil.lock(lockKey);
        try {
            Long count = bookkeepingWalletDao.lambdaQuery().eq(BookkeepingWalletEntity::getUserId, userAddBo.getUserId()).count();
            if (count > 0) {
                log.info("用户[{}]已存在钱包表数据, 默认跳过初始化用户钱包表数据操作", userAddBo.getUserId());
                return;
            }
            BookkeepingWalletEntity bookkeepingWalletEntity = new BookkeepingWalletEntity();
            bookkeepingWalletEntity.setWalletBalance(BigDecimal.ZERO);
            bookkeepingWalletEntity.setWalletAssets(BigDecimal.ZERO);
            bookkeepingWalletEntity.setUserId(userAddBo.getUserId());
            bookkeepingWalletDao.save(bookkeepingWalletEntity);
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
        log.info("用户[{}]钱包表初始化完成", userAddBo.getUserId());
    }
}
