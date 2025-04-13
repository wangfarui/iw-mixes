package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.DetailVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 记账记录 详情VO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class BookkeepingRecordDetailVo implements DetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 记录日期
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordDate;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime recordTime;

    /**
     * 记录类型
     */
    private RecordCategoryEnum recordCategory;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记录标签
     */
    private List<Integer> recordTags;

    /**
     * 是否为激励记录
     */
    private Integer isExcitationRecord;

    /**
     * 是否计入统计
     */
    private Integer isStatistics;

    /**
     * 记录图标
     */
    private String recordIcon;
}
