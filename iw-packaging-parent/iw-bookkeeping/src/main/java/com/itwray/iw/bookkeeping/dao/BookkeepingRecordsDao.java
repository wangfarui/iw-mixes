package com.itwray.iw.bookkeeping.dao;

import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsMapper;
import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsYearlyStatisticsMapper;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingBarChartStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsYearStatisticsQueryDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingStatisticsTypeEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewMonthlyVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewSummaryVo;
import com.itwray.iw.bookkeeping.utils.BookkeepingStatisticsUtils;
import com.itwray.iw.web.dao.BaseDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 记账记录表 DAO
 *
 * @author wray
 * @since 2024/8/28
 */
@Component
public class BookkeepingRecordsDao extends BaseDao<BookkeepingRecordsMapper, BookkeepingRecordsEntity> {

    private BookkeepingRecordsYearlyStatisticsMapper bookkeepingRecordsYearlyStatisticsMapper;

    @Autowired
    public void setBookkeepingRecordsYearlyStatisticsMapper(BookkeepingRecordsYearlyStatisticsMapper bookkeepingRecordsYearlyStatisticsMapper) {
        this.bookkeepingRecordsYearlyStatisticsMapper = bookkeepingRecordsYearlyStatisticsMapper;
    }

    public BookkeepingRecordsYearlyStatisticsMapper getStatisticsMapper() {
        return this.bookkeepingRecordsYearlyStatisticsMapper;
    }

    public BookkeepingRecordsOverviewSummaryVo queryYearlyOverviewSummary(BookkeepingRecordsYearStatisticsQueryDto dto) {
        BookkeepingRecordsOverviewSummaryVo summaryVo = BookkeepingRecordsOverviewSummaryVo.empty();
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);

        // 查询支出
        if (CollectionUtils.isNotEmpty(dto.getRecordCategories()) && dto.getRecordCategories().contains(RecordCategoryEnum.CONSUME)) {
            statisticsDto.setRecordCategory(RecordCategoryEnum.CONSUME);
            BookkeepingStatisticsTotalVo bookkeepingStatisticsTotalVo = getBaseMapper().totalStatistics(statisticsDto);
            if (bookkeepingStatisticsTotalVo != null) {
                summaryVo.setTotalConsume(bookkeepingStatisticsTotalVo.getTotalAmount());
                summaryVo.setConsumeCount(bookkeepingStatisticsTotalVo.getTotalRecordNum());
            }
        }
        // 查询收入
        if (CollectionUtils.isNotEmpty(dto.getRecordCategories()) && dto.getRecordCategories().contains(RecordCategoryEnum.INCOME)) {
            statisticsDto.setRecordCategory(RecordCategoryEnum.INCOME);
            BookkeepingStatisticsTotalVo bookkeepingStatisticsTotalVo = getBaseMapper().totalStatistics(statisticsDto);
            if (bookkeepingStatisticsTotalVo != null) {
                summaryVo.setTotalIncome(bookkeepingStatisticsTotalVo.getTotalAmount());
                summaryVo.setIncomeCount(bookkeepingStatisticsTotalVo.getTotalRecordNum());
            }
        }

        summaryVo.setNetIncome(summaryVo.getTotalIncome().subtract(summaryVo.getTotalConsume()));

        return summaryVo;
    }

    public BookkeepingRecordsOverviewMonthlyVo queryYearlyOverviewMonthly(BookkeepingRecordsYearStatisticsQueryDto dto) {
        BookkeepingRecordsOverviewMonthlyVo monthlyVo = BookkeepingRecordsOverviewMonthlyVo.empty();
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        statisticsDto.setStatisticsType(BookkeepingStatisticsTypeEnum.YEAR);

        int count = 0;
        // 查询支出
        if (CollectionUtils.isNotEmpty(dto.getRecordCategories()) && dto.getRecordCategories().contains(RecordCategoryEnum.CONSUME)) {
            statisticsDto.setRecordCategory(RecordCategoryEnum.CONSUME);
            List<BookkeepingBarChartStatisticsBo> barChartStatistics = getBaseMapper().barChartStatistics(statisticsDto);
            if (CollectionUtils.isNotEmpty(barChartStatistics)) {
                monthlyVo.setConsumeTrendData(BookkeepingStatisticsUtils.convertToBarChartYearStatisticsBo(dto.getStartDate(), barChartStatistics));
            }

            count++;
        }
        // 查询收入
        if (CollectionUtils.isNotEmpty(dto.getRecordCategories()) && dto.getRecordCategories().contains(RecordCategoryEnum.INCOME)) {
            statisticsDto.setRecordCategory(RecordCategoryEnum.INCOME);
            List<BookkeepingBarChartStatisticsBo> barChartStatistics = getBaseMapper().barChartStatistics(statisticsDto);
            if (CollectionUtils.isNotEmpty(barChartStatistics)) {
                monthlyVo.setIncomeTrendData(BookkeepingStatisticsUtils.convertToBarChartYearStatisticsBo(dto.getStartDate(), barChartStatistics));
            }

            count++;
        }

        // 表示统计支出和收入，需要计算出净收入
        if (count == 2) {
            List<BigDecimal> netIncomeTrendData = new ArrayList<>();
            for (int i = 0; i < monthlyVo.getIncomeTrendData().size(); i++) {
                netIncomeTrendData.add(monthlyVo.getIncomeTrendData().get(i).subtract(monthlyVo.getConsumeTrendData().get(i)));
            }
            monthlyVo.setNetIncomeTrendData(netIncomeTrendData);
        }

        return monthlyVo;
    }

    private BookkeepingStatisticsDto buildStatisticsDto(BookkeepingRecordsYearStatisticsQueryDto dto) {
        BookkeepingStatisticsDto statisticsDto = new BookkeepingStatisticsDto();
        statisticsDto.setCurrentStartMonth(dto.getStartDate());
        statisticsDto.setCurrentEndMonth(dto.getEndDate());
        statisticsDto.setIsSearchAll(dto.getIgnoreNotStatistics());
        return statisticsDto;
    }
}
