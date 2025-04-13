package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 获取汇率 DTO
 *
 * @author wray
 * @since 2025/4/12
 */
@Data
@Schema(name = "获取汇率 请求参数")
public class GetExchangeRateDto {

    @Schema(title = "转换前货币")
    @NotBlank
    private String fromCurrency;

    @Schema(title = "转换后货币")
    @NotBlank
    private String toCurrency;

    @Schema(title = "汇率日期")
    private LocalDate queryDate;

    @Schema(title = "转换前金额")
    private BigDecimal fromAmount;
}
