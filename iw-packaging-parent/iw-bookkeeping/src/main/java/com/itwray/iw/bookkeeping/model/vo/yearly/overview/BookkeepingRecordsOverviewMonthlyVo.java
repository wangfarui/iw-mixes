package com.itwray.iw.bookkeeping.model.vo.yearly.overview;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 月度趋势数据（12个月，按1月-12月顺序）
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsOverviewMonthlyVo {

    private static final List<BigDecimal> EMPTY_LIST = new ArrayList<>();

    static {
        for (int i = 0; i < 12; i++) {
            EMPTY_LIST.add(BigDecimal.ZERO);
        }
    }

    /**
     * 每月支出
     */
    private List<BigDecimal> consumeTrendData;

    /**
     * 每月收入
     */
    private List<BigDecimal> incomeTrendData;

    /**
     * 每月净收入
     */
    private List<BigDecimal> netIncomeTrendData;

    public static BookkeepingRecordsOverviewMonthlyVo empty() {
        BookkeepingRecordsOverviewMonthlyVo monthlyVo = new BookkeepingRecordsOverviewMonthlyVo();
        monthlyVo.setConsumeTrendData(EMPTY_LIST);
        monthlyVo.setIncomeTrendData(EMPTY_LIST);
        monthlyVo.setNetIncomeTrendData(EMPTY_LIST);
        return monthlyVo;
    }
}
