package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.itwray.iw.bookkeeping.dao.BookkeepingBudgetDao;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.mapper.BookkeepingBudgetMapper;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetUpdateDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeMonthStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingBudgetEntity;
import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetStatisticsVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsTotalVo;
import com.itwray.iw.bookkeeping.service.BookkeepingBudgetService;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 记账预算表 服务实现类
 *
 * @author wray
 * @since 2025-04-24
 */
@Service
public class BookkeepingBudgetServiceImpl extends WebServiceImpl<BookkeepingBudgetDao, BookkeepingBudgetMapper, BookkeepingBudgetEntity,
        BookkeepingBudgetAddDto, BookkeepingBudgetUpdateDto, BookkeepingBudgetDetailVo, Integer> implements BookkeepingBudgetService {

    private final BookkeepingRecordsDao bookkeepingRecordsDao;

    @Autowired
    public BookkeepingBudgetServiceImpl(BookkeepingBudgetDao baseDao, BookkeepingRecordsDao bookkeepingRecordsDao) {
        super(baseDao);
        this.bookkeepingRecordsDao = bookkeepingRecordsDao;
    }

    @Override
    public Integer add(BookkeepingBudgetAddDto dto) {
        boolean isTotalBudgetType = dto.getBudgetType().isTotalBudgetType();
        if (!isTotalBudgetType && dto.getRecordType() == null) {
            throw new BusinessException("分类预算的记录分类不能为空");
        }

        // 新增前先查询用户是否已经添加过相同类型的预算
        Long count = getBaseDao().lambdaQuery()
                .eq(BookkeepingBudgetEntity::getBudgetType, dto.getBudgetType())
                .eq(!isTotalBudgetType, BookkeepingBudgetEntity::getRecordType, dto.getRecordType())
                .count();
        if (count > 0) {
            throw new BusinessException("已经存在相同类型的预算, 请勿重复添加");
        }

        return super.add(dto);
    }

    @Override
    public BookkeepingBudgetStatisticsVo getTotalBudget(BudgetTypeEnum budgetType) {
        if (!budgetType.isTotalBudgetType()) {
            throw new BusinessException("查询类型不是总预算类型");
        }
        BookkeepingBudgetStatisticsVo vo = new BookkeepingBudgetStatisticsVo();

        BookkeepingBudgetEntity budgetEntity = getBaseDao().lambdaQuery()
                .eq(BookkeepingBudgetEntity::getBudgetType, budgetType)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (budgetEntity == null) {
            // 预算金额返回0,表示未设置预算
            vo.setBudgetAmount(BigDecimal.ZERO);
            return vo;
        }

        vo.setBudgetType(budgetType);
        vo.setId(budgetEntity.getId());
        vo.setBudgetAmount(budgetEntity.getBudgetAmount());

        // 查询总支出金额
        BookkeepingConsumeMonthStatisticsDto dto = new BookkeepingConsumeMonthStatisticsDto();
        dto.setRecordCategory(RecordCategoryEnum.CONSUME);
        dto.setCurrentStartMonth(budgetType.equals(BudgetTypeEnum.MONTH) ? DateUtils.startDateOfNowMonth() : DateUtils.startDateOfNowYear());
        dto.setCurrentEndMonth(budgetType.equals(BudgetTypeEnum.MONTH) ? DateUtils.endDateOfNowMonth() : DateUtils.endDateOfNowYear());
        BookkeepingConsumeStatisticsTotalVo statisticsTotalVo = bookkeepingRecordsDao.getBaseMapper().totalStatistics(dto);

        vo.setUsedAmount(statisticsTotalVo.getTotalAmount());
        vo.setRemainingAmount(budgetEntity.getBudgetAmount().subtract(statisticsTotalVo.getTotalAmount()));
        vo.setUsedRatio(vo.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0
                ? new BigDecimal("100")
                : statisticsTotalVo.getTotalAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : statisticsTotalVo.getTotalAmount().multiply(new BigDecimal("100")).divide(budgetEntity.getBudgetAmount(), 2, RoundingMode.HALF_UP));

        return vo;
    }

    @Override
    public List<BookkeepingBudgetStatisticsVo> getCategoryBudget(BudgetTypeEnum budgetType) {
        if (budgetType.isTotalBudgetType()) {
            throw new BusinessException("查询类型不是分类预算类型");
        }

        List<BookkeepingBudgetEntity> budgetEntityList = getBaseDao().lambdaQuery()
                .eq(BookkeepingBudgetEntity::getBudgetType, budgetType)
                .list();

        if (CollUtil.isEmpty(budgetEntityList)) {
            return Collections.emptyList();
        }

        BookkeepingConsumeCategoryStatisticsDto dto = new BookkeepingConsumeCategoryStatisticsDto();
        dto.setRecordCategory(RecordCategoryEnum.CONSUME);
        dto.setCurrentStartMonth(budgetType.equals(BudgetTypeEnum.MONTH_CATEGORY) ? DateUtils.startDateOfNowMonth() : DateUtils.startDateOfNowYear());
        dto.setCurrentEndMonth(budgetType.equals(BudgetTypeEnum.MONTH_CATEGORY) ? DateUtils.endDateOfNowMonth() : DateUtils.endDateOfNowYear());
        dto.setRecordTypeList(budgetEntityList.stream().map(BookkeepingBudgetEntity::getRecordType).toList());
        Map<Integer, BigDecimal> recordTypeMap = bookkeepingRecordsDao.getBaseMapper().categoryStatistics(dto)
                .stream()
                .collect(Collectors.toMap(BookkeepingConsumeStatisticsCategoryVo::getRecordType, BookkeepingConsumeStatisticsCategoryVo::getAmount));

        List<BookkeepingBudgetStatisticsVo> voList = new ArrayList<>();
        for (BookkeepingBudgetEntity budgetEntity : budgetEntityList) {
            BookkeepingBudgetStatisticsVo vo = new BookkeepingBudgetStatisticsVo();
            vo.setBudgetType(budgetType);
            vo.setId(budgetEntity.getId());
            vo.setRecordType(budgetEntity.getRecordType());
            vo.setBudgetAmount(budgetEntity.getBudgetAmount());
            vo.setUsedAmount(recordTypeMap.getOrDefault(budgetEntity.getRecordType(), BigDecimal.ZERO));
            vo.setRemainingAmount(budgetEntity.getBudgetAmount().subtract(vo.getUsedAmount()));
            vo.setUsedRatio(vo.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0
                    ? new BigDecimal("100")
                    : vo.getUsedAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : vo.getUsedAmount().multiply(new BigDecimal("100")).divide(budgetEntity.getBudgetAmount(), 2, RoundingMode.HALF_UP));
            voList.add(vo);
        }

        return voList;
    }
}
