package com.itwray.iw.bookkeeping.service.impl;

import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingBarChartStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingIncomeStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingStatisticsDto;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
import com.itwray.iw.bookkeeping.service.BookkeepingIncomeService;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 记账收入 服务实现层
 *
 * @author farui.wang
 * @since 2025/5/27
 */
@Service
public class BookkeepingIncomeServiceImpl implements BookkeepingIncomeService {

    private final BookkeepingRecordsDao bookkeepingRecordsDao;

    @Autowired
    public BookkeepingIncomeServiceImpl(BookkeepingRecordsDao bookkeepingRecordsDao) {
        this.bookkeepingRecordsDao = bookkeepingRecordsDao;
    }

    @Override
    public BookkeepingStatisticsTotalVo totalStatistics(BookkeepingIncomeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        return bookkeepingRecordsDao.getBaseMapper().totalStatistics(statisticsDto);
    }

    @Override
    public List<BookkeepingStatisticsRankVo> rankStatistics(BookkeepingIncomeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        return bookkeepingRecordsDao.getBaseMapper().rankStatistics(statisticsDto);
    }

    @Override
    public List<BigDecimal> chartStatistics(BookkeepingIncomeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        List<BookkeepingBarChartStatisticsBo> list = bookkeepingRecordsDao.getBaseMapper().barChartStatistics(statisticsDto);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, BigDecimal> recordDateMap = list.stream().collect(Collectors.toMap(
                BookkeepingBarChartStatisticsBo::getRecordDate, BookkeepingBarChartStatisticsBo::getAmount
        ));
        List<BigDecimal> result = new ArrayList<>();
        switch (dto.getStatisticsType()) {
            case MONTH -> {
                LocalDate startDate = statisticsDto.getCurrentStartMonth();
                while (!startDate.isAfter(statisticsDto.getCurrentEndMonth())) {
                    String s = DateUtils.formatLocalDate(startDate);
                    result.add(Optional.ofNullable(recordDateMap.get(s)).orElse(BigDecimal.ZERO));
                    startDate = startDate.plusDays(1);
                }
            }
            case YEAR -> {
                int year = statisticsDto.getCurrentStartMonth().getYear();
                for (int i = 1; i <= 12; i++) {
                    String recordDate = year + "-" + (i < 10 ? "0" + i : i);
                    result.add(Optional.ofNullable(recordDateMap.get(recordDate)).orElse(BigDecimal.ZERO));
                }
            }
        }
        return result;
    }

    private BookkeepingStatisticsDto buildStatisticsDto(BookkeepingIncomeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = new BookkeepingStatisticsDto();
        statisticsDto.setCurrentMonth(Optional.ofNullable(dto.getCurrentMonth()).orElse(LocalDate.now()));
        switch (dto.getStatisticsType()) {
            case MONTH -> {
                statisticsDto.setCurrentStartMonth(DateUtils.startDateOfMonth(statisticsDto.getCurrentMonth()));
                statisticsDto.setCurrentEndMonth(DateUtils.endDateOfMonth(statisticsDto.getCurrentMonth()));
            }
            case YEAR -> {
                statisticsDto.setCurrentStartMonth(DateUtils.startDateOfYear(statisticsDto.getCurrentMonth()));
                statisticsDto.setCurrentEndMonth(DateUtils.endDateOfYear(statisticsDto.getCurrentMonth()));
            }
            default -> throw new BusinessException("无效的统计类型");
        }
        statisticsDto.setRecordCategory(RecordCategoryEnum.INCOME);
        statisticsDto.setStatisticsType(dto.getStatisticsType());
        return statisticsDto;
    }
}
