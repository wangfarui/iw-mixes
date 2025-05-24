package com.itwray.iw.bookkeeping.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新钱包余额DTO
 *
 * @author wray
 * @since 2025/5/24
 */
@Data
@Schema(name = "更新钱包余额DTO")
public class BookkeepingWalletBalanceUpdateDto {

    @Schema(title = "钱包余额")
    @NotNull(message = "钱包余额不能为空")
    private BigDecimal walletBalance;
}
