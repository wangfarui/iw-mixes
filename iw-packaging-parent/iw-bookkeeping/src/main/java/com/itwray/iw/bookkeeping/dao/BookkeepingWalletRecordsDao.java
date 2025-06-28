package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletRecordsEntity;
import com.itwray.iw.bookkeeping.mapper.BookkeepingWalletRecordsMapper;
import com.itwray.iw.bookkeeping.model.enums.WalletRecordsChangeTypeEnum;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 用户钱包记录表 DAO
 *
 * @author wray
 * @since 2025-05-26
 */
@Component
public class BookkeepingWalletRecordsDao extends BaseDao<BookkeepingWalletRecordsMapper, BookkeepingWalletRecordsEntity> {

    /**
     * 保存变动记录
     *
     * @param changeTypeEnum 变动类型
     * @param beforeAmount   变动前金额
     * @param changeAmount   变动金额
     */
    public void saveChangeRecord(WalletRecordsChangeTypeEnum changeTypeEnum, BigDecimal beforeAmount, BigDecimal changeAmount) {
        BookkeepingWalletRecordsEntity entity = new BookkeepingWalletRecordsEntity();
        entity.setChangeType(changeTypeEnum);
        entity.setBeforeAmount(beforeAmount);
        entity.setAfterAmount(beforeAmount.add(changeAmount));
        entity.setChangeAmount(changeAmount);
        entity.setUserId(UserUtils.getUserId());

        this.save(entity);
    }
}
