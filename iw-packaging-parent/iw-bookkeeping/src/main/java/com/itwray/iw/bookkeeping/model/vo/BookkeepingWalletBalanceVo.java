package com.itwray.iw.bookkeeping.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 钱包余额VO
 *
 * @author wray
 * @since 2025/4/24
 */
@Data
@Schema(name = "钱包余额")
public class BookkeepingWalletBalanceVo {

    @Schema(title = "钱包id")
    private Integer id;

    @Schema(title = "钱包余额")
    private BigDecimal walletBalance;
}
