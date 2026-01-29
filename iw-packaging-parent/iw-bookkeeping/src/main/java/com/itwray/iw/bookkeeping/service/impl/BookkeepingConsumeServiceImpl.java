package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.auth.client.BaseDictClient;
import com.itwray.iw.auth.model.vo.DictListVo;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingBarChartStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsYearStatisticsQueryDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingStatisticsDto;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.consume.BookkeepingRecordsConsumeTagsVo;
import com.itwray.iw.bookkeeping.service.BookkeepingConsumeService;
import com.itwray.iw.bookkeeping.utils.BookkeepingStatisticsUtils;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
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

    private BaseDictClient baseDictClient;

    @Autowired
    public BookkeepingConsumeServiceImpl(BookkeepingRecordsDao bookkeepingRecordsDao) {
        this.bookkeepingRecordsDao = bookkeepingRecordsDao;
    }

    @Autowired
    public void setBaseDictClient(BaseDictClient baseDictClient) {
        this.baseDictClient = baseDictClient;
    }

    @Override
    public BookkeepingStatisticsTotalVo totalStatistics(BookkeepingConsumeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        return bookkeepingRecordsDao.getBaseMapper().totalStatistics(statisticsDto);
    }

    @Override
    public List<BookkeepingStatisticsRankVo> rankStatistics(BookkeepingConsumeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        return bookkeepingRecordsDao.getBaseMapper().rankStatistics(statisticsDto);
    }

    @Override
    public List<BookkeepingConsumeStatisticsCategoryVo> pieChartStatistics(BookkeepingConsumeCategoryStatisticsDto dto) {
        this.buildStatisticsDto(dto);
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
            dto.setCurrentStartMonth(null);
            dto.setCurrentEndMonth(null);
            this.buildStatisticsDto(dto);
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

    @Override
    public List<BigDecimal> barChartStatistics(BookkeepingConsumeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        List<BookkeepingBarChartStatisticsBo> list = bookkeepingRecordsDao.getBaseMapper().barChartStatistics(statisticsDto);
        List<BigDecimal> result;
        switch (dto.getStatisticsType()) {
            case YEAR -> result = BookkeepingStatisticsUtils.convertToBarChartYearStatisticsBo(dto.getCurrentStartMonth(), list);
            case MONTH -> result = BookkeepingStatisticsUtils.convertToBarChartMonthStatisticsBo(dto.getCurrentMonth(), list);
            default -> result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<BookkeepingRecordsConsumeTagsVo> tagsStatistics(BookkeepingConsumeStatisticsDto dto) {
        BookkeepingStatisticsDto statisticsDto = this.buildStatisticsDto(dto);
        BookkeepingRecordsYearStatisticsQueryDto yearStatisticsQueryDto = new BookkeepingRecordsYearStatisticsQueryDto();
        yearStatisticsQueryDto.setStartDate(statisticsDto.getCurrentStartMonth());
        yearStatisticsQueryDto.setEndDate(statisticsDto.getCurrentEndMonth());
        yearStatisticsQueryDto.setIgnoreNotStatistics(dto.getIsSearchAll());
        yearStatisticsQueryDto.setRecordCategories(new HashSet<>(Collections.singleton(RecordCategoryEnum.CONSUME)));
        yearStatisticsQueryDto.setUserId(UserUtils.getUserId());
        List<BookkeepingRecordsConsumeTagsVo> consumeTagsVos = bookkeepingRecordsDao.getStatisticsMapper().statisticsTagConsume(yearStatisticsQueryDto);
        if (CollectionUtils.isNotEmpty(consumeTagsVos)) {
            BigDecimal totalAmount = consumeTagsVos.stream().map(BookkeepingRecordsConsumeTagsVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            int totalCount = consumeTagsVos.stream().map(BookkeepingRecordsConsumeTagsVo::getCount).reduce(0, Integer::sum);

            // 查询记账标签字典值
            List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TAG_CONSUME.getCode());
            Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getId, DictListVo::getDictName));
            for (BookkeepingRecordsConsumeTagsVo consumeTagsVo : consumeTagsVos) {
                consumeTagsVo.setName(dictMap.get(consumeTagsVo.getDictId()));
                consumeTagsVo.setRatio(new BigDecimal(consumeTagsVo.getCount() * 100).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP));
                consumeTagsVo.setAmountRatio(consumeTagsVo.getAmount().multiply(new BigDecimal("100")).divide(totalAmount, 2, RoundingMode.HALF_UP));
            }
        }
        return consumeTagsVos;
    }

    private BookkeepingStatisticsDto buildStatisticsDto(BookkeepingConsumeStatisticsDto dto) {
        if (dto.getCurrentMonth() == null) {
            dto.setCurrentMonth(LocalDate.now());
        }
        if (dto.getStatisticsType() != null) {
            switch (dto.getStatisticsType()) {
                case MONTH -> {
                    dto.setCurrentStartMonth(DateUtils.startDateOfMonth(dto.getCurrentMonth()));
                    dto.setCurrentEndMonth(DateUtils.endDateOfMonth(dto.getCurrentMonth()));
                }
                case YEAR -> {
                    dto.setCurrentStartMonth(DateUtils.startDateOfYear(dto.getCurrentMonth()));
                    dto.setCurrentEndMonth(DateUtils.endDateOfYear(dto.getCurrentMonth()));
                }
                default -> throw new BusinessException("无效的统计类型");
            }
        }
        dto.setRecordCategory(RecordCategoryEnum.CONSUME);
        return BeanUtil.copyProperties(dto, BookkeepingStatisticsDto.class);
    }
}
