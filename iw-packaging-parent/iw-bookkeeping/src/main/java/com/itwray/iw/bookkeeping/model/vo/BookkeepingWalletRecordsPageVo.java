package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 钱包记录分页响应对象
 *
 * @author farui.wang
 * @since 2025/5/26
 */
@Data
@Schema(name = "钱包记录分页响应对象")
public class BookkeepingWalletRecordsPageVo {

    /**
     * id
     */
    private Integer id;

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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;
}
