package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户钱包表
 *
 * @author wray
 * @since 2025-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_wallet")
public class BookkeepingWalletEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 余额
     */
    private BigDecimal walletBalance;

    /**
     * 资产
     */
    private BigDecimal walletAssets;

}
