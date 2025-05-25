package com.itwray.iw.bookkeeping.service.impl;

import com.itwray.iw.bookkeeping.dao.BookkeepingWalletDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsWalletBalanceDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletBalanceUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletEntity;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletBalanceVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletService;
import com.itwray.iw.starter.redis.lock.RedisLockUtil;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.model.enums.mq.BookkeepingRecordsTopicEnum;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 用户钱包表 服务实现类
 *
 * @author wray
 * @since 2025-05-22
 */
@Service
@RocketMQMessageListener(consumerGroup = "bookkeeping-records-service", topic = BookkeepingRecordsTopicEnum.TOPIC, tag = "wallet_balance")
public class BookkeepingWalletServiceImpl implements BookkeepingWalletService, RocketMQClientListener<BookkeepingRecordsWalletBalanceDto> {

    private final BookkeepingWalletDao bookkeepingWalletDao;

    @Autowired
    public BookkeepingWalletServiceImpl(BookkeepingWalletDao bookkeepingWalletDao) {
        this.bookkeepingWalletDao = bookkeepingWalletDao;
    }

    @Override
    public Class<BookkeepingRecordsWalletBalanceDto> getGenericClass() {
        return BookkeepingRecordsWalletBalanceDto.class;
    }

    @Override
    public void doConsume(BookkeepingRecordsWalletBalanceDto dto) {
        String lockKey = BookkeepingRecordsTopicEnum.WALLET_BALANCE.getDestination() + ":" +dto.getUserId();
        RedisLockUtil.lock(lockKey);
        try {
            int updateCount = bookkeepingWalletDao.getBaseMapper().updateWalletBalance(dto.getUserId(), dto.getAmount());
            if (updateCount <= 0) {
                BookkeepingWalletEntity entity = new BookkeepingWalletEntity();
                entity.setWalletBalance(dto.getAmount());
                bookkeepingWalletDao.save(entity);
            }
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
    }

    @Override
    public BookkeepingWalletBalanceVo getWalletBalance() {
        BookkeepingWalletEntity bookkeepingWalletEntity = bookkeepingWalletDao.lambdaQuery()
                .eq(BookkeepingWalletEntity::getUserId, UserUtils.getUserId())
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        BookkeepingWalletBalanceVo vo = new BookkeepingWalletBalanceVo();
        if (bookkeepingWalletEntity != null) {
            vo.setId(bookkeepingWalletEntity.getId());
            vo.setWalletBalance(bookkeepingWalletEntity.getWalletBalance());
        } else {
            vo.setWalletBalance(BigDecimal.ZERO);
        }
        return vo;
    }

    @Override
    public void updateBalance(BookkeepingWalletBalanceUpdateDto dto) {
        String lockKey = BookkeepingRecordsTopicEnum.WALLET_BALANCE.getDestination() + ":" +UserUtils.getUserId();
        RedisLockUtil.lock(lockKey);
        try {
            boolean bool = bookkeepingWalletDao.lambdaUpdate()
                    .eq(BookkeepingWalletEntity::getUserId, UserUtils.getUserId())
                    .set(BookkeepingWalletEntity::getWalletBalance, dto.getWalletBalance())
                    .update();
            if (!bool) {
                BookkeepingWalletEntity entity = new BookkeepingWalletEntity();
                entity.setWalletBalance(dto.getWalletBalance());
                bookkeepingWalletDao.save(entity);
            }
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
    }
}
