package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账收入 新增DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class IncomesAddDto {

    /**
     * 收入时间
     * <p>为空时，表示当前时间</p>
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime incomeDate;

    /**
     * 收入来源
     */
    @NotNull(message = "收入来源不能为空")
    @Length(min = 1, max = 64, message = "收入来源数据长度要求在1-64之间")
    private String incomeSource;

    /**
     * 收入金额
     */
    @NotNull(message = "收入金额不能为空")
    private BigDecimal incomeAmount;

    /**
     * 备注
     */
    private String remark;
}
