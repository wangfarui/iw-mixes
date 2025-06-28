package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.WalletRecordsChangeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新钱包金额DTO
 *
 * @author wray
 * @since 2025/5/24
 */
@Data
@Schema(name = "更新钱包金额DTO")
public class BookkeepingWalletAmountUpdateDto {

    @Schema(title = "变动类型")
    @NotNull(message = "变动类型不能为空")
    private WalletRecordsChangeTypeEnum changeType;

    @Schema(title = "更新金额")
    @NotNull(message = "更新金额不能为空")
    private BigDecimal updateAmount;
}
