package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeMonthStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsTotalVo;
import com.itwray.iw.bookkeeping.service.BookkeepingConsumeService;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.constants.WebCommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 记账支出 服务实现层
 *
 * @author wray
 * @since 2024/10/15
 */
@Service
public class BookkeepingConsumeServiceImpl implements BookkeepingConsumeService {

    private final BookkeepingRecordsDao bookkeepingRecordsDao;

    @Autowired
    public BookkeepingConsumeServiceImpl(BookkeepingRecordsDao bookkeepingRecordsDao) {
        this.bookkeepingRecordsDao = bookkeepingRecordsDao;
    }

    @Override
    public BookkeepingConsumeStatisticsTotalVo totalStatistics(BookkeepingConsumeMonthStatisticsDto dto) {
        this.fillMonthStatisticsDto(dto);
        return bookkeepingRecordsDao.getBaseMapper().totalStatistics(dto);
    }

    @Override
    public List<BookkeepingConsumeStatisticsRankVo> rankStatistics(BookkeepingConsumeMonthStatisticsDto dto) {
        this.fillMonthStatisticsDto(dto);
        return bookkeepingRecordsDao.lambdaQuery()
                .eq(BookkeepingRecordsEntity::getRecordCategory, dto.getRecordCategory())
                // 如果不查询所有,则只查询计入统计的数据
                .eq(BoolEnum.FALSE.getCode().equals(dto.getIsSearchAll()), BookkeepingRecordsEntity::getIsStatistics, BoolEnum.TRUE.getCode())
                .between(BookkeepingRecordsEntity::getRecordDate, dto.getCurrentStartMonth(), dto.getCurrentEndMonth())
                .select(BookkeepingRecordsEntity::getId, BookkeepingRecordsEntity::getRecordSource,
                        BookkeepingRecordsEntity::getRecordTime, BookkeepingRecordsEntity::getAmount)
                .orderByDesc(BookkeepingRecordsEntity::getAmount)
                .last(WebCommonConstants.standardLimit(dto.getLimit()))
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, BookkeepingConsumeStatisticsRankVo.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookkeepingConsumeStatisticsCategoryVo> categoryStatistics(BookkeepingConsumeCategoryStatisticsDto dto) {
        this.fillMonthStatisticsDto(dto);
        // 查询当前月度的分类支出统计数据
        List<BookkeepingConsumeStatisticsCategoryVo> currentMonthCategoryVoList = bookkeepingRecordsDao.getBaseMapper().categoryStatistics(dto);
        // 计算总支出
        BigDecimal totalAmount = currentMonthCategoryVoList.stream()
                .map(BookkeepingConsumeStatisticsCategoryVo::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 计算各分类支出金额的占比
        for (BookkeepingConsumeStatisticsCategoryVo vo : currentMonthCategoryVoList) {
            vo.setRatio(vo.getAmount().divide(totalAmount, 5, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(WebCommonConstants.AMOUNT_SCALE, RoundingMode.HALF_UP));
        }

        // 是否查询上个月的统计数据
        if (Boolean.TRUE.equals(dto.getIsQueryLastMonth())) {
            dto.setCurrentMonth(dto.getCurrentMonth().minusMonths(1L));
            this.fillMonthStatisticsDto(dto);
            // 查询上个月度的分类支出统计数据
            List<BookkeepingConsumeStatisticsCategoryVo> lastMonthCategoryVoList = bookkeepingRecordsDao.getBaseMapper().categoryStatistics(dto);
            // key -> recordType, value -> amount
            Map<Integer, BigDecimal> lastMonthStatisticsMap = lastMonthCategoryVoList.stream()
                    .collect(Collectors.toMap(BookkeepingConsumeStatisticsCategoryVo::getRecordType, BookkeepingConsumeStatisticsCategoryVo::getAmount));
            for (BookkeepingConsumeStatisticsCategoryVo vo : currentMonthCategoryVoList) {
                // 根据记录分类获取上个月的金额
                BigDecimal lastMonthAmount = lastMonthStatisticsMap.get(vo.getRecordType());
                if (lastMonthAmount != null) {
                    // 判断当前月的金额是否大于等于上个月的基恩
                    vo.setIsGreaterThan(vo.getAmount().compareTo(lastMonthAmount) >= 0);
                    // 计算相差金额
                    if (vo.getIsGreaterThan()) {
                        vo.setLastAmount(vo.getAmount().subtract(lastMonthAmount));
                    } else {
                        vo.setLastAmount(lastMonthAmount.subtract(vo.getAmount()));
                    }
                } else {
                    // 上个月没有该分类支出，所以必定为大于
                    vo.setIsGreaterThan(true);
                    vo.setLastAmount(vo.getAmount());
                }
            }
            // 将当前月没有的分类支出，而上个月有的分类支出按照金额降序追加到列表末尾
            Set<Integer> currentMonthRecordTypeMap = currentMonthCategoryVoList.stream()
                    .map(BookkeepingConsumeStatisticsCategoryVo::getRecordType)
                    .collect(Collectors.toSet());
            for (BookkeepingConsumeStatisticsCategoryVo vo : lastMonthCategoryVoList) {
                if (!currentMonthRecordTypeMap.contains(vo.getRecordType())) {
                    BookkeepingConsumeStatisticsCategoryVo t = new BookkeepingConsumeStatisticsCategoryVo();
                    t.setRecordType(vo.getRecordType());
                    t.setAmount(BigDecimal.ZERO);
                    t.setIsGreaterThan(false);
                    t.setLastAmount(vo.getAmount());
                    currentMonthCategoryVoList.add(t);
                }
            }
        }

        return currentMonthCategoryVoList;
    }

    private void fillMonthStatisticsDto(BookkeepingConsumeMonthStatisticsDto dto) {
        if (dto.getCurrentMonth() == null) {
            dto.setCurrentMonth(LocalDate.now());
        }
        dto.setCurrentStartMonth(DateUtils.startDateOfMonth(dto.getCurrentMonth()));
        dto.setCurrentEndMonth(DateUtils.endDateOfMonth(dto.getCurrentMonth()));
        dto.setRecordCategory(RecordCategoryEnum.CONSUME);
    }
}
