package com.itwray.iw.external.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 货币汇率表
 *
 * @author wray
 * @since 2025-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("external_exchange_rate")
public class ExternalExchangeRateEntity extends IdEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 转换前货币
     */
    private String fromCurrency;

    /**
     * 转换后货币
     */
    private String toCurrency;

    /**
     * 汇率
     * <p>按照 货币值 == 1 计算</p>
     */
    private BigDecimal exchangeRate;

    /**
     * 查询日期
     */
    private LocalDate queryDate;

    /**
     * 转换前金额
     */
    private BigDecimal fromAmount;

    /**
     * 转换后金额
     */
    private BigDecimal toAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
