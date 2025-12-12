package com.itwray.iw.bookkeeping.model.vo.yearly.overview;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.constants.CommonConstants;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 记账习惯数据
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsOverviewHabitsVo {

    /**
     * 一年内的记账天数（0-365）
     */
    private Integer recordingDays;

    /**
     * 连续记账最长天数
     */
    private Integer maxContinuousDays;

    /**
     * 最长连续记账开始日期
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate maxContinuousStartDate;

    /**
     * 最长连续记账结束日期
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate maxContinuousEndDate;

    /**
     * 记账次数最多的月份（例如 "12月"）
     */
    private String peakMonth;

    /**
     * 该月份的记账次数
     */
    private Integer peakCount;

    /**
     * 遗漏次数（应该有记账但没有）
     */
    private Integer missingCount;

    /**
     * 遗漏率（百分比，0-100）
     */
    private BigDecimal missingRate;

    /**
     * 一年内的记账次数
     */
    private Long recordingCount;

    /**
     * 平均每天记账次数（保留1-2位小数）
     */
    private BigDecimal avgPerDay;

    /**
     * 激励性评价文案
     */
    private String evaluation;

    public static BookkeepingRecordsOverviewHabitsVo empty() {
        BookkeepingRecordsOverviewHabitsVo habitsVo = new BookkeepingRecordsOverviewHabitsVo();
        habitsVo.setRecordingDays(0);
        habitsVo.setMaxContinuousDays(0);
        habitsVo.setMaxContinuousStartDate(null);
        habitsVo.setMaxContinuousEndDate(null);
        habitsVo.setPeakMonth(CommonConstants.EMPTY);
        habitsVo.setPeakCount(0);
        habitsVo.setMissingCount(0);
        habitsVo.setMissingRate(BigDecimal.ZERO);
        habitsVo.setAvgPerDay(BigDecimal.ZERO);
        habitsVo.setEvaluation("冒得评价");
        return habitsVo;
    }
}
