package com.itwray.iw.bookkeeping.model.vo.yearly.consume;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.AbstractUserOwnerVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 支出Top10
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingRecordsConsumeTopVo extends AbstractUserOwnerVo {

    /**
     * 分类名称
     */
    private String category;

    /**
     * 日期（YYYY-MM-DD格式）
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate date;

    /**
     * 描述文案
     */
    private String description;

    /**
     * 金额（数字或字符串）
     */
    private BigDecimal amount;
}
