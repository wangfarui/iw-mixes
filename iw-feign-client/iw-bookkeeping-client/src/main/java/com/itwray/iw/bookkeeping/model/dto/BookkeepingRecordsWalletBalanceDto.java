package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.web.model.dto.UserDto;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账记录-同步钱包余额
 *
 * @author farui.wang
 * @since 2025/5/22
 */
@Data
public class BookkeepingRecordsWalletBalanceDto implements UserDto {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 变动金额(正负数都有)
     */
    private BigDecimal amount;
}
