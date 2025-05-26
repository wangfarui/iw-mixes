package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.bookkeeping.dao.BookkeepingWalletDao;
import com.itwray.iw.bookkeeping.dao.BookkeepingWalletRecordsDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsWalletBalanceDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingWalletAmountUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletEntity;
import com.itwray.iw.bookkeeping.model.enums.WalletRecordsChangeTypeEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingWalletDetailVo;
import com.itwray.iw.bookkeeping.service.BookkeepingWalletService;
import com.itwray.iw.starter.redis.lock.RedisLockUtil;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.enums.mq.BookkeepingRecordsTopicEnum;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final BookkeepingWalletRecordsDao bookkeepingWalletRecordsDao;

    @Autowired
    public BookkeepingWalletServiceImpl(BookkeepingWalletDao bookkeepingWalletDao, BookkeepingWalletRecordsDao bookkeepingWalletRecordsDao) {
        this.bookkeepingWalletDao = bookkeepingWalletDao;
        this.bookkeepingWalletRecordsDao = bookkeepingWalletRecordsDao;
    }

    @Override
    public Class<BookkeepingRecordsWalletBalanceDto> getGenericClass() {
        return BookkeepingRecordsWalletBalanceDto.class;
    }

    @Override
    @Transactional
    public void doConsume(BookkeepingRecordsWalletBalanceDto dto) {
        String lockKey = BookkeepingRecordsTopicEnum.WALLET_AMOUNT.getDestination() + ":" + dto.getUserId();
        RedisLockUtil.lock(lockKey);
        try {
            UserUtils.setUserId(dto.getUserId());
            // 查询用户钱包
            BookkeepingWalletEntity walletEntity = bookkeepingWalletDao.queryByUserId(dto.getUserId());
            // 更新用户余额
            bookkeepingWalletDao.getBaseMapper().updateWalletBalance(dto.getUserId(), dto.getAmount());
            // 新增余额变动记录
            bookkeepingWalletRecordsDao.saveChangeRecord(WalletRecordsChangeTypeEnum.BALANCE, walletEntity.getWalletBalance(), dto.getAmount());
        } finally {
            UserUtils.removeUserId();
            RedisLockUtil.unlock(lockKey);
        }
    }

    @Override
    public BookkeepingWalletDetailVo getUserWalletDetail() {
        BookkeepingWalletEntity walletEntity = bookkeepingWalletDao.queryByUserId(UserUtils.getUserId());
        return BeanUtil.copyProperties(walletEntity, BookkeepingWalletDetailVo.class);
    }

    @Override
    @Transactional
    public void updateAmount(BookkeepingWalletAmountUpdateDto dto) {
        String lockKey = BookkeepingRecordsTopicEnum.WALLET_AMOUNT.getDestination() + ":" + UserUtils.getUserId();
        RedisLockUtil.lock(lockKey);
        try {
            // 查询用户钱包
            BookkeepingWalletEntity walletEntity = bookkeepingWalletDao.queryByUserId(UserUtils.getUserId());
            // 更新用户钱包金额
            bookkeepingWalletDao.lambdaUpdate()
                    .eq(BookkeepingWalletEntity::getUserId, UserUtils.getUserId())
                    .set(WalletRecordsChangeTypeEnum.BALANCE.equals(dto.getChangeType()), BookkeepingWalletEntity::getWalletBalance, dto.getUpdateAmount())
                    .set(WalletRecordsChangeTypeEnum.ASSETS.equals(dto.getChangeType()), BookkeepingWalletEntity::getWalletAssets, dto.getUpdateAmount())
                    .update();
            BigDecimal beforeAmount;
            switch (dto.getChangeType()) {
                case BALANCE -> beforeAmount = walletEntity.getWalletBalance();
                case ASSETS -> beforeAmount = walletEntity.getWalletAssets();
                default -> throw new BusinessException("不支持的操作");
            }
            // 新增金额变动记录
            bookkeepingWalletRecordsDao.saveChangeRecord(dto.getChangeType(), beforeAmount, dto.getUpdateAmount().subtract(beforeAmount));
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
    }
}
