package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.bookkeeping.model.enums.WalletRecordsChangeTypeEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户钱包记录表
 *
 * @author wray
 * @since 2025-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_wallet_records")
public class BookkeepingWalletRecordsEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 变动类型
     */
    private WalletRecordsChangeTypeEnum changeType;

    /**
     * 变动金额
     */
    private BigDecimal changeAmount;

    /**
     * 变动前金额
     */
    private BigDecimal beforeAmount;

    /**
     * 变动后金额
     */
    private BigDecimal afterAmount;
}
