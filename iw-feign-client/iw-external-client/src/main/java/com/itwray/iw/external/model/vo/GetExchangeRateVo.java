package com.itwray.iw.external.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 获取汇率 VO
 *
 * @author wray
 * @since 2025/4/12
 */
@Data
@Schema(name = "获取汇率 响应参数")
public class GetExchangeRateVo {

    @Schema(title = "转换前货币")
    private String fromCurrency;

    @Schema(title = "转换后货币")
    private String toCurrency;

    @Schema(title = "汇率")
    private BigDecimal exchangeRate;

    @Schema(title = "汇率日期")
    private LocalDate queryDate;

    @Schema(title = "转换前金额")
    private BigDecimal fromAmount;

    @Schema(title = "转换后金额")
    private BigDecimal toAmount;

}
