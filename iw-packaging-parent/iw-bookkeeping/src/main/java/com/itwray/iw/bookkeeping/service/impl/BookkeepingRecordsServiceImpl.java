package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.idev.excel.FastExcel;
import com.itwray.iw.auth.client.AuthUserClient;
import com.itwray.iw.auth.client.BaseDictClient;
import com.itwray.iw.auth.model.vo.DictListVo;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.excel.listener.BookkeepingRecordsImportDataListener;
import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsMapper;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingRecordsImportBo;
import com.itwray.iw.bookkeeping.model.bo.RecordsStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.*;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingBudgetEntity;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingRecordTypeDefaultEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.*;
import com.itwray.iw.bookkeeping.model.vo.yearly.consume.*;
import com.itwray.iw.bookkeeping.model.vo.yearly.income.*;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewHabitsVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewMonthlyVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewSummaryVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsYearStatisticsOverviewVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.external.client.InternalApiClient;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.dao.BaseBusinessFileDao;
import com.itwray.iw.web.dao.BaseDictBusinessRelationDao;
import com.itwray.iw.web.dao.BaseDictDao;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import com.itwray.iw.web.model.enums.BusinessFileTypeEnum;
import com.itwray.iw.web.model.enums.DictBusinessTypeEnum;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.model.enums.OrderNoEnum;
import com.itwray.iw.web.model.enums.mq.BookkeepingRecordsTopicEnum;
import com.itwray.iw.web.model.enums.mq.PointsRecordsTopicEnum;
import com.itwray.iw.web.model.vo.FileVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.OrderNoUtils;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 记录表 服务实现层
 *
 * @author wray
 * @since 2024/8/28
 */
@Service
@Slf4j
public class BookkeepingRecordsServiceImpl extends WebServiceImpl<BookkeepingRecordsDao, BookkeepingRecordsMapper, BookkeepingRecordsEntity,
        BookkeepingRecordAddDto, BookkeepingRecordUpdateDto, BookkeepingRecordDetailVo, Integer> implements BookkeepingRecordsService {

    private final BaseDictBusinessRelationDao baseDictBusinessRelationDao;

    private final BaseBusinessFileDao baseBusinessFileDao;

    private BaseDictDao baseDictDao;

    private InternalApiClient internalApiClient;

    private BaseDictClient baseDictClient;

    private AuthUserClient authUserClient;

    @Autowired
    public BookkeepingRecordsServiceImpl(BookkeepingRecordsDao baseDao,
                                         BaseDictBusinessRelationDao baseDictBusinessRelationDao,
                                         BaseBusinessFileDao baseBusinessFileDao) {
        super(baseDao);
        this.baseDictBusinessRelationDao = baseDictBusinessRelationDao;
        this.baseBusinessFileDao = baseBusinessFileDao;
    }

    @Autowired
    public void setBaseDictDao(BaseDictDao baseDictDao) {
        this.baseDictDao = baseDictDao;
    }

    @Autowired
    public void setInternalApiClient(InternalApiClient internalApiClient) {
        this.internalApiClient = internalApiClient;
    }

    @Autowired
    public void setBaseDictClient(BaseDictClient baseDictClient) {
        this.baseDictClient = baseDictClient;
    }

    @Autowired
    public void setAuthUserClient(AuthUserClient authUserClient) {
        this.authUserClient = authUserClient;
    }

    @Override
    @Transactional
    public Integer add(BookkeepingRecordAddDto dto) {
        BookkeepingRecordsEntity bookkeepingRecords = this.buildBookkeepingRecordAddDto(dto);

        // 生成订单号
        bookkeepingRecords.setOrderNo(OrderNoUtils.getAndIncrement(OrderNoEnum.BOOKKEEPING_RECORDS));

        // 保存记账记录
        getBaseDao().save(bookkeepingRecords);

        // 保存标签
        baseDictBusinessRelationDao.saveRelation(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG, bookkeepingRecords.getId(), dto.getRecordTags());

        // 保存记账附件
        baseBusinessFileDao.saveBusinessFile(bookkeepingRecords.getId(), BusinessFileTypeEnum.BOOKKEEPING_RECORDS, dto.getFileList());

        // 记录为激励收入时，积分+1
        if (RecordCategoryEnum.INCOME.equals(dto.getRecordCategory())
                && BoolEnum.TRUE.getCode().equals(dto.getIsExcitationRecord())) {
            this.addPointsRecordsByExcitation(bookkeepingRecords.getOrderNo());
        }

        // 同步用户钱包余额
        this.syncWalletBalance(dto.getRecordCategory(), bookkeepingRecords.getAmount());

        return bookkeepingRecords.getId();
    }

    @Override
    @Transactional
    public void update(BookkeepingRecordUpdateDto dto) {
        BookkeepingRecordsEntity bookkeepingRecordsEntity = getBaseDao().queryById(dto.getId());
        if (!bookkeepingRecordsEntity.getRecordCategory().equals(dto.getRecordCategory())) {
            throw new BusinessException("不支持修改记账记录类型操作");
        }

        // 修改标签
        baseDictBusinessRelationDao.saveRelation(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG, dto.getId(), dto.getRecordTags());

        // 保存记账附件
        baseBusinessFileDao.saveBusinessFile(dto.getId(), BusinessFileTypeEnum.BOOKKEEPING_RECORDS, dto.getFileList());

        // 记账记录类型为收入类型时
        if (RecordCategoryEnum.INCOME.equals(dto.getRecordCategory())) {
            // 如果修改了激励记录状态, 则同步积分数据
            if (!bookkeepingRecordsEntity.getIsExcitationRecord().equals(dto.getIsExcitationRecord())) {
                if (BoolEnum.TRUE.getCode().equals(dto.getIsExcitationRecord())) {
                    this.addPointsRecordsByExcitation(bookkeepingRecordsEntity.getOrderNo());
                } else {
                    this.deductPointsRecordsByExcitation(bookkeepingRecordsEntity.getOrderNo());
                }
            }
        }

        BookkeepingRecordsEntity recordsEntity = this.buildBookkeepingRecordAddDto(dto);
        getBaseDao().updateById(recordsEntity);

        // 同步用户钱包余额
        if (bookkeepingRecordsEntity.getAmount().compareTo(recordsEntity.getAmount()) != 0) {
            this.syncWalletBalance(dto.getRecordCategory(), recordsEntity.getAmount().subtract(bookkeepingRecordsEntity.getAmount()));
        }
    }

    private BookkeepingRecordsEntity buildBookkeepingRecordAddDto(BookkeepingRecordAddDto dto) {
        BookkeepingRecordsEntity bookkeepingRecords = BeanUtil.copyProperties(dto, BookkeepingRecordsEntity.class);

        // 记录日期为空是默认取当前时间
        if (bookkeepingRecords.getRecordDate() == null) {
            bookkeepingRecords.setRecordDate(LocalDate.now());
            bookkeepingRecords.setRecordTime(LocalDateTime.now());
        } else {
            // 日期取指定日期，时间取当前时间
            bookkeepingRecords.setRecordTime(bookkeepingRecords.getRecordDate().atTime(LocalTime.now()));
        }

        // 如果货币类型不为空，则转换货币
        if (StringUtils.isNotBlank(dto.getFromCurrency())) {
            GetExchangeRateDto exchangeRateDto = new GetExchangeRateDto();
            exchangeRateDto.setFromCurrency(dto.getFromCurrency());
            exchangeRateDto.setToCurrency("CNY");
            exchangeRateDto.setQueryDate(dto.getRecordDate());
            exchangeRateDto.setFromAmount(dto.getAmount());
            Object exchangeRateVo = internalApiClient.getExchangeRate(exchangeRateDto);
            if (exchangeRateVo != null) {
                if (exchangeRateVo instanceof Map map) {
                    bookkeepingRecords.setAmount(new BigDecimal(map.get("toAmount").toString()).setScale(2, RoundingMode.HALF_UP));
                }
            }
        }

        return bookkeepingRecords;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        BookkeepingRecordsEntity bookkeepingRecordsEntity = getBaseDao().queryById(id);
        super.delete(id);

        // 删除标签
        baseDictBusinessRelationDao.removeRelation(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG, id);

        // 删除记账附件
        baseBusinessFileDao.removeBusinessFile(id, BusinessFileTypeEnum.BOOKKEEPING_RECORDS);

        // 同步积分数据
        if (RecordCategoryEnum.INCOME.equals(bookkeepingRecordsEntity.getRecordCategory())
                && BoolEnum.TRUE.getCode().equals(bookkeepingRecordsEntity.getIsExcitationRecord())) {
            this.deductPointsRecordsByExcitation(bookkeepingRecordsEntity.getOrderNo());
        }

        // 同步用户钱包余额
        this.syncWalletBalance(bookkeepingRecordsEntity.getRecordCategory(), bookkeepingRecordsEntity.getAmount().negate());
    }

    @Override
    public BookkeepingRecordDetailVo detail(Integer id) {
        BookkeepingRecordDetailVo vo = super.detail(id);

        // 查询标签
        List<Integer> tagIdList = baseDictBusinessRelationDao.queryDictIdList(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG, id);
        vo.setRecordTags(tagIdList);

        // 查询记账附件
        List<FileVo> fileVoList = baseBusinessFileDao.getBusinessFile(id, BusinessFileTypeEnum.BOOKKEEPING_RECORDS);
        vo.setFileList(fileVoList);

        return vo;
    }

    @Override
    public PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto) {
        this.processBookkeepingRecordPageDto(dto);

        PageVo<BookkeepingRecordPageVo> pageVo = new PageVo<>(dto);
        getBaseDao().getBaseMapper().page(pageVo, dto);

        LocalDate now = LocalDate.now();
        int nowYear = now.getYear();
        DateTimeFormatter oldYearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter nowYearFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        pageVo.getRecords().forEach(t -> {
            // 格式化记账日期
            LocalDate localDate = t.getRecordTime().toLocalDate();
            if (now.equals(localDate)) {
                t.setRecordTimeStr("今天 " + t.getRecordTime().toLocalTime().format(timeFormatter));
            } else if (now.equals(localDate.plusDays(1))) {
                t.setRecordTimeStr("昨天 " + t.getRecordTime().toLocalTime().format(timeFormatter));
            } else if (nowYear == localDate.getYear()) {
                t.setRecordTimeStr(t.getRecordTime().format(nowYearFormatter));
            } else {
                t.setRecordTimeStr(t.getRecordTime().format(oldYearFormatter));
            }
        });

        return pageVo;
    }

    @Override
    public List<BookkeepingRecordPageVo> list(BookkeepingRecordListDto dto) {
        // 记账日期为空时，默认查当天
        if (dto.getRecordDate() == null) {
            dto.setRecordDate(LocalDate.now());
        }
        return getBaseDao().lambdaQuery()
                .eq(BookkeepingRecordsEntity::getRecordDate, dto.getRecordDate())
                .orderByDesc(BookkeepingRecordsEntity::getId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, BookkeepingRecordPageVo.class))
                .collect(Collectors.toList());
    }

    private void processBookkeepingRecordPageDto(BookkeepingRecordPageDto dto) {
        if (dto.getRecordStartDate() == null) {
            if (dto.getRecordEndDate() != null) {
                dto.setRecordStartDate(DateUtils.startDateOfMonth(dto.getRecordEndDate()));
            }
        }
        if (dto.getRecordEndDate() == null) {
            if (dto.getRecordStartDate() != null) {
                dto.setRecordEndDate(DateUtils.endDateOfMonth(dto.getRecordStartDate()));
            }
        }
        if (CollUtil.isNotEmpty(dto.getTagIdList())) {
            dto.setTagBusinessType(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG.getCode());
        }
    }

    @Override
    public BookkeepingRecordsStatisticsVo statistics(BookkeepingRecordsStatisticsDto dto) {
        if (dto.getRecordStartDate() == null && dto.getRecordEndDate() == null) {
            dto.setRecordStartDate(DateUtils.startDateOfNowMonth());
            dto.setRecordEndDate(DateUtils.endDateOfNowMonth());
        }
        this.processBookkeepingRecordPageDto(dto);

        // 查询记录类型对应的总金额
        Map<Integer, BigDecimal> statisticsMap = getBaseDao().getBaseMapper().statistics(dto)
                .stream()
                .collect(Collectors.toMap(RecordsStatisticsBo::getRecordCategory, RecordsStatisticsBo::getTotalAmount));

        BookkeepingRecordsStatisticsVo statisticsVo = new BookkeepingRecordsStatisticsVo();
        // 消费金额
        statisticsVo.setConsume(statisticsMap.getOrDefault(RecordCategoryEnum.CONSUME.getCode(), BigDecimal.ZERO));
        // 收入金额
        statisticsVo.setIncome(statisticsMap.getOrDefault(RecordCategoryEnum.INCOME.getCode(), BigDecimal.ZERO));
        return statisticsVo;
    }

    @Override
    @Transactional
    public void importRecords(MultipartFile file) {
        // 查询当前用户的记账-记录分类字典项
        List<BaseDictEntity> dictEntityList = baseDictDao.queryDictEntityList(DictTypeEnum.BOOKKEEPING_RECORD_TYPE);
        Map<String, Integer> dictNameMap = dictEntityList.stream().collect(Collectors.toMap(BaseDictEntity::getDictName, BaseDictEntity::getDictCode));
        BookkeepingRecordsImportDataListener listener = new BookkeepingRecordsImportDataListener(UserUtils.getUserId(), dictNameMap);
        try {
            FastExcel.read(file.getInputStream(), BookkeepingRecordsImportBo.class, listener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            log.error("importRecords 导入记账记录异常", e);
            throw new IwWebException(e);
        }
    }

    @Override
    public void processImportData(BookkeepingRecordsImportBo bo, Map<String, Integer> dictNameMap) {
        if (bo == null) {
            return;
        }
        if (bo.getRecordCategory() == null) {
            return;
        }
        BookkeepingRecordTypeDefaultEnum recordTypeDefaultEnum = BookkeepingRecordTypeDefaultEnum.confirmRecordType(bo.getRecordTypeDesc());
        if (BookkeepingRecordTypeDefaultEnum.IGNORE.equals(recordTypeDefaultEnum)) {
            return;
        }
        Integer recordTypeCode = dictNameMap.get(recordTypeDefaultEnum.getName());
        if (recordTypeCode == null) {
            recordTypeCode = dictNameMap.get(BookkeepingRecordTypeDefaultEnum.OTHER.getName());
        }
        if (recordTypeCode == null) {
            log.warn("BookkeepingRecordsService#processImportData 无法确定记录分类的字典值, bo: {}", bo.getRecordTypeDesc());
            return;
        }
        BookkeepingRecordsEntity recordsEntity = new BookkeepingRecordsEntity();
        recordsEntity.setOrderNo(OrderNoUtils.getAndIncrement(OrderNoEnum.BOOKKEEPING_RECORDS));
        recordsEntity.setRecordDate(bo.getRecordTime().toLocalDate());
        recordsEntity.setRecordTime(bo.getRecordTime());
        recordsEntity.setRecordCategory(bo.getRecordCategory());
        boolean isFillRemark = true;
        if (bo.getRemark() == null) {
            recordsEntity.setRecordSource("消费");
        } else if (bo.getRemark().length() < 50) {
            recordsEntity.setRecordSource(bo.getRemark());
            isFillRemark = false;
        } else {
            recordsEntity.setRecordSource(bo.getRemark().substring(0, 50));
        }
        recordsEntity.setAmount(bo.getAmount());
        recordsEntity.setRecordType(recordTypeCode);
        if (isFillRemark) {
            recordsEntity.setRemark(bo.getRemark().length() < 255 ? bo.getRemark() : bo.getRemark().substring(0, 255));
        }
        recordsEntity.setUserId(bo.getUserId());
        getBaseDao().save(recordsEntity);
    }

    @Override
    public void syncBookkeepingPointsByBudget(List<BookkeepingBudgetEntity> monthBudgetList) {
        if (CollectionUtils.isEmpty(monthBudgetList)) {
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[yyyy年MM月]");
        // 根据用户维度, 统计每个用户不同记账分类下的支出统计
        Map<Integer, List<BookkeepingBudgetEntity>> userBudgetMap = monthBudgetList.stream()
                .collect(Collectors.groupingBy(BookkeepingBudgetEntity::getUserId));
        for (Map.Entry<Integer, List<BookkeepingBudgetEntity>> entry : userBudgetMap.entrySet()) {
            Integer userId = entry.getKey();
            String userToken = authUserClient.genericUserToken(userId);
            try {
                UserUtils.setUserId(userId);
                UserUtils.setToken(userToken);
                // 查询记账分类字典值
                List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
                Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getDictCode, DictListVo::getDictName));
                for (BookkeepingBudgetEntity budgetEntity : entry.getValue()) {
                    BookkeepingRecordsStatisticsDto statisticsDto = new BookkeepingRecordsStatisticsDto();
                    statisticsDto.setRecordStartDate(DateUtils.startDateOfMonth(budgetEntity.getBudgetMonth()));
                    statisticsDto.setRecordEndDate(DateUtils.endDateOfMonth(budgetEntity.getBudgetMonth()));
                    statisticsDto.setRecordType(budgetEntity.getRecordType());
                    // 统计预算所在月份下, 指定记账分类的实际支出情况
                    BookkeepingRecordsStatisticsVo statisticsVo = this.statistics(statisticsDto);
                    // 判断是否满足预算
                    boolean stayBudget = statisticsVo.getConsume().compareTo(budgetEntity.getBudgetAmount()) <= 0;
                    // 根据预算结果确定积分变动数量
                    Integer points = stayBudget ? budgetEntity.getRewardPoints() : budgetEntity.getPunishPoints();
                    PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
                    pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.getCodeByPoints(points));
                    pointsRecordsAddDto.setPoints(points);
                    pointsRecordsAddDto.setSource(
                            budgetEntity.getBudgetMonth().format(dateTimeFormatter) + "\"" +
                                    dictMap.get(budgetEntity.getRecordType()) + "\"" +
                                    (stayBudget ? "符合预算" : "超出预算")
                    );
                    pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING_BUDGET_MONTH.getCode());
                    pointsRecordsAddDto.setUserId(UserUtils.getUserId());
                    MQProducerHelper.send(PointsRecordsTopicEnum.BOOKKEEPING_SERVICE, pointsRecordsAddDto);
                }
            } finally {
                UserUtils.removeUserId();
                UserUtils.removeUserToken();
            }
        }
    }

    @Override
    public BookkeepingRecordsYearStatisticsOverviewVo yearStatisticsOverview(BookkeepingRecordsYearStatisticsQueryDto dto) {
        this.computeYearStatisticsParam(dto);
        dto.setRecordCategories(new HashSet<>(Arrays.asList(RecordCategoryEnum.CONSUME, RecordCategoryEnum.INCOME)));

        BookkeepingRecordsYearStatisticsOverviewVo overviewVo = new BookkeepingRecordsYearStatisticsOverviewVo();

        // 查询年度总览-汇总数据
        BookkeepingRecordsOverviewSummaryVo summaryVo = getBaseDao().queryYearlyOverviewSummary(dto);
        overviewVo.setYearStatistics(summaryVo);

        // 查询年度总览-月度趋势数据
        BookkeepingRecordsOverviewMonthlyVo monthlyVo = getBaseDao().queryYearlyOverviewMonthly(dto);
        overviewVo.setMonthlyData(monthlyVo);

        // 查询年度总览-记账习惯数据
        BookkeepingRecordsOverviewHabitsVo habitsVo = BookkeepingRecordsOverviewHabitsVo.empty();
        // 一年内的记账天数
        Integer recordingDays = getBaseDao().getStatisticsMapper().statisticsRecordingDays(dto);
        habitsVo.setRecordingDays(recordingDays == null ? 0 : recordingDays);
        // 连续记账最长天数
        BookkeepingRecordsOverviewHabitsVo maxContinuousDaysVo = getBaseDao().getStatisticsMapper().statisticsMaxContinuousDays(dto);
        if (maxContinuousDaysVo != null) {
            habitsVo.setMaxContinuousDays(maxContinuousDaysVo.getMaxContinuousDays());
            habitsVo.setMaxContinuousStartDate(maxContinuousDaysVo.getMaxContinuousStartDate());
            habitsVo.setMaxContinuousEndDate(maxContinuousDaysVo.getMaxContinuousEndDate());
        }
        // 记账次数最多的月份
        BookkeepingRecordsOverviewHabitsVo peakMonthVo = getBaseDao().getStatisticsMapper().statisticsPeakMonth(dto);
        if (peakMonthVo != null) {
            habitsVo.setPeakMonth(peakMonthVo.getPeakMonth() + "月");
            habitsVo.setPeakCount(peakMonthVo.getPeakCount());
        }
        // 遗漏次数、平均每天记账次数、文案
        Integer missingCount = getBaseDao().getStatisticsMapper().statisticsMissingCount(dto);
        habitsVo.setMissingCount(missingCount == null ? 0 : missingCount);
        Long recordingCount = getBaseDao().lambdaQuery()
                .between(BookkeepingRecordsEntity::getRecordDate, dto.getStartDate(), dto.getEndDate())
                .eq(dto.getIgnoreNotStatistics() != null && dto.getIgnoreNotStatistics() == 0, BookkeepingRecordsEntity::getIsStatistics, 1)
                .count();
        if (habitsVo.getRecordingDays() == 0 || habitsVo.getMissingCount() == 0 || recordingCount == 0) {
            habitsVo.setMissingRate(BigDecimal.ZERO);
        } else {
            habitsVo.setMissingRate(new BigDecimal(habitsVo.getMissingCount() * 100).divide(new BigDecimal(recordingCount), 2, RoundingMode.HALF_UP));
        }
        habitsVo.setRecordingCount(recordingCount);
        if (recordingCount == 0) {
            habitsVo.setAvgPerDay(BigDecimal.ZERO);
        } else {
            habitsVo.setAvgPerDay(new BigDecimal(recordingCount).divide(new BigDecimal("365"), 2, RoundingMode.HALF_UP));
        }
        if (habitsVo.getMaxContinuousDays() == 0) {
            habitsVo.setEvaluation("一天天的，帐都不记，行不行啊小老弟❓");
        } else {
            habitsVo.setEvaluation(String.format("坚持记账的好习惯！连续记账超过%d天是值得表扬的成就。保持这个节奏，你会更好地掌握自己的财务状况。💪", habitsVo.getMaxContinuousDays()));
        }
        overviewVo.setRecordingHabits(habitsVo);

        return overviewVo;
    }

    @Override
    public BookkeepingRecordsYearStatisticsConsumeVo yearStatisticsConsume(BookkeepingRecordsYearStatisticsQueryDto dto) {
        this.computeYearStatisticsParam(dto);
        dto.setRecordCategories(new HashSet<>(Collections.singleton(RecordCategoryEnum.CONSUME)));

        BookkeepingRecordsYearStatisticsConsumeVo consumeVo = new BookkeepingRecordsYearStatisticsConsumeVo();

        // 查询年度支出-汇总数据
        BookkeepingRecordsOverviewSummaryVo overviewSummaryVo = getBaseDao().queryYearlyOverviewSummary(dto);
        BookkeepingRecordsConsumeSummaryVo consumeSummaryVo = new BookkeepingRecordsConsumeSummaryVo();
        consumeSummaryVo.setTotalConsume(overviewSummaryVo.getTotalConsume());
        consumeSummaryVo.setConsumeCount(overviewSummaryVo.getConsumeCount());
        consumeVo.setYearStatistics(consumeSummaryVo);

        // 查询年度支出-月度趋势
        BookkeepingRecordsOverviewMonthlyVo monthlyVo = getBaseDao().queryYearlyOverviewMonthly(dto);
        consumeVo.setMonthlyData(monthlyVo.getConsumeTrendData());

        // 查询年度支出-支出分类占比
        BookkeepingConsumeCategoryStatisticsDto categoryStatisticsDto = new BookkeepingConsumeCategoryStatisticsDto();
        categoryStatisticsDto.setRecordCategory(RecordCategoryEnum.CONSUME);
        categoryStatisticsDto.setCurrentStartMonth(dto.getStartDate());
        categoryStatisticsDto.setCurrentEndMonth(dto.getEndDate());
        categoryStatisticsDto.setIsSearchAll(dto.getIgnoreNotStatistics());
        List<BookkeepingConsumeStatisticsCategoryVo> statisticsCategoryVos = getBaseDao().getBaseMapper().categoryStatistics(categoryStatisticsDto);
        if (CollectionUtils.isNotEmpty(statisticsCategoryVos)) {
            BigDecimal totalAmount = statisticsCategoryVos.stream().map(BookkeepingConsumeStatisticsCategoryVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<BookkeepingRecordsConsumeCategoriesVo> categoryVoList = new ArrayList<>();
            // 查询记账分类字典值
            List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
            Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getDictCode, DictListVo::getDictName));
            for (BookkeepingConsumeStatisticsCategoryVo statisticsCategoryVo : statisticsCategoryVos) {
                BookkeepingRecordsConsumeCategoriesVo categoriesVo = new BookkeepingRecordsConsumeCategoriesVo();
                categoriesVo.setName(dictMap.get(statisticsCategoryVo.getRecordType()));
                categoriesVo.setAmount(statisticsCategoryVo.getAmount());
                categoriesVo.setRatio(statisticsCategoryVo.getAmount().multiply(new BigDecimal("100")).divide(totalAmount, 2, RoundingMode.HALF_UP));
                categoryVoList.add(categoriesVo);
            }
            consumeVo.setConsumeCategories(categoryVoList);
        }

        // 查询年度支出-支出标签占比
        List<BookkeepingRecordsConsumeTagsVo> consumeTagsVos = getBaseDao().getStatisticsMapper().statisticsTagConsume(dto);
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
        consumeVo.setConsumeTags(consumeTagsVos);

        // 查询年度支出-Top10
        BookkeepingStatisticsDto statisticsDto = new BookkeepingStatisticsDto();
        statisticsDto.setRecordCategory(RecordCategoryEnum.CONSUME);
        statisticsDto.setCurrentStartMonth(dto.getStartDate());
        statisticsDto.setCurrentEndMonth(dto.getEndDate());
        statisticsDto.setIsSearchAll(dto.getIgnoreNotStatistics());
        List<BookkeepingStatisticsRankVo> rankVoList = getBaseDao().getBaseMapper().rankStatistics(statisticsDto);
        if (CollectionUtils.isNotEmpty(rankVoList)) {
            // 查询记账分类字典值
            List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
            Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getDictCode, DictListVo::getDictName));
            List<BookkeepingRecordsConsumeTopVo> topConsumeList = new ArrayList<>();
            for (BookkeepingStatisticsRankVo rankVo : rankVoList) {
                BookkeepingRecordsConsumeTopVo topVo = new BookkeepingRecordsConsumeTopVo();
                topVo.setCategory(dictMap.get(rankVo.getRecordType()));
                topVo.setDescription(rankVo.getRecordSource());
                topVo.setDate(rankVo.getRecordDate());
                topVo.setAmount(rankVo.getAmount());
                topConsumeList.add(topVo);
            }
            consumeVo.setTopConsumeList(topConsumeList);
        }

        // 查询年度支出-支出洞察
        BookkeepingRecordsConsumeInsightsVo insightsVo = new BookkeepingRecordsConsumeInsightsVo();
        BookkeepingRecordsConsumeInsightsVo consumeDaysVo = getBaseDao().getStatisticsMapper().statisticsMaxDay(dto);
        if (consumeDaysVo != null) {
            insightsVo.setMaxDayAmount(consumeDaysVo.getMaxDayAmount());
            insightsVo.setMaxDayDate(consumeDaysVo.getMaxDayDate());
        }
        BookkeepingRecordsConsumeInsightsVo consumeMonthVo = getBaseDao().getStatisticsMapper().statisticsMaxMonth(dto);
        if (consumeMonthVo != null) {
            insightsVo.setMaxMonthAmount(consumeMonthVo.getMaxMonthAmount());
            insightsVo.setMaxMonthName(consumeMonthVo.getMaxMonthName() + "月");
        }
        if (CollectionUtils.isNotEmpty(consumeVo.getConsumeTags())) {
            BookkeepingRecordsConsumeTagsVo tagsOneVo = consumeVo.getConsumeTags().get(0);
            BookkeepingRecordsConsumeTagsVo tagsLastVo = consumeVo.getConsumeTags().get(consumeVo.getConsumeTags().size() - 1);
            insightsVo.setTopTag(tagsOneVo.getName());
            insightsVo.setTopTagCount(tagsOneVo.getCount());
            insightsVo.setTopTagAmount(tagsOneVo.getAmount());
            insightsVo.setBottomTag(tagsLastVo.getName());
            insightsVo.setBottomTagCount(tagsLastVo.getCount());
            insightsVo.setBottomTagAmount(tagsLastVo.getAmount());
        }
        Long count = getBaseDao().lambdaQuery()
                .between(BookkeepingRecordsEntity::getRecordDate, dto.getStartDate(), dto.getEndDate())
                .eq(dto.getIgnoreNotStatistics() != null && dto.getIgnoreNotStatistics() == 0, BookkeepingRecordsEntity::getIsStatistics, 1)
                .eq(BookkeepingRecordsEntity::getRecordCategory, RecordCategoryEnum.CONSUME)
                .ge(BookkeepingRecordsEntity::getAmount, new BigDecimal("100"))
                .count();
        if (count == 0) {
            insightsVo.setLargeExpenseRatio(BigDecimal.ZERO);
        } else {
            insightsVo.setLargeExpenseRatio(new BigDecimal(count * 100).divide(new BigDecimal(consumeVo.getYearStatistics().getConsumeCount()), 2, RoundingMode.HALF_UP));
        }
        insightsVo.setAvgMonthAmount(consumeVo.getYearStatistics().getTotalConsume().divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP));
        consumeVo.setInsights(insightsVo);

        return consumeVo;
    }

    @Override
    public BookkeepingRecordsYearStatisticsIncomeVo yearStatisticsIncome(BookkeepingRecordsYearStatisticsQueryDto dto) {
        this.computeYearStatisticsParam(dto);
        dto.setRecordCategories(new HashSet<>(Collections.singleton(RecordCategoryEnum.INCOME)));

        BookkeepingRecordsYearStatisticsIncomeVo incomeVo = new BookkeepingRecordsYearStatisticsIncomeVo();

        // 查询年度收入-汇总数据
        BookkeepingRecordsOverviewSummaryVo overviewSummaryVo = getBaseDao().queryYearlyOverviewSummary(dto);
        BookkeepingRecordsIncomeSummaryVo incomeSummaryVo = new BookkeepingRecordsIncomeSummaryVo();
        incomeSummaryVo.setTotalIncome(overviewSummaryVo.getTotalIncome());
        incomeSummaryVo.setIncomeCount(overviewSummaryVo.getIncomeCount());
        incomeVo.setYearStatistics(incomeSummaryVo);

        // 查询年度收入-月度趋势
        BookkeepingRecordsOverviewMonthlyVo monthlyVo = getBaseDao().queryYearlyOverviewMonthly(dto);
        incomeVo.setMonthlyData(monthlyVo.getIncomeTrendData());

        // 查询年度收入-收入分类占比
        BookkeepingConsumeCategoryStatisticsDto categoryStatisticsDto = new BookkeepingConsumeCategoryStatisticsDto();
        categoryStatisticsDto.setRecordCategory(RecordCategoryEnum.INCOME);
        categoryStatisticsDto.setCurrentStartMonth(dto.getStartDate());
        categoryStatisticsDto.setCurrentEndMonth(dto.getEndDate());
        categoryStatisticsDto.setIsSearchAll(dto.getIgnoreNotStatistics());
        List<BookkeepingConsumeStatisticsCategoryVo> statisticsCategoryVos = getBaseDao().getBaseMapper().categoryStatistics(categoryStatisticsDto);
        if (CollectionUtils.isNotEmpty(statisticsCategoryVos)) {
            BigDecimal totalAmount = statisticsCategoryVos.stream().map(BookkeepingConsumeStatisticsCategoryVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<BookkeepingRecordsIncomeCategoriesVo> categoryVoList = new ArrayList<>();
            // 查询记账分类字典值
            List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
            Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getDictCode, DictListVo::getDictName));
            for (BookkeepingConsumeStatisticsCategoryVo statisticsCategoryVo : statisticsCategoryVos) {
                BookkeepingRecordsIncomeCategoriesVo categoriesVo = new BookkeepingRecordsIncomeCategoriesVo();
                categoriesVo.setName(dictMap.get(statisticsCategoryVo.getRecordType()));
                categoriesVo.setAmount(statisticsCategoryVo.getAmount());
                categoriesVo.setRatio(statisticsCategoryVo.getAmount().multiply(new BigDecimal("100")).divide(totalAmount, 2, RoundingMode.HALF_UP));
                categoryVoList.add(categoriesVo);
            }
            incomeVo.setIncomeCategories(categoryVoList);
        }

        // 查询年度收入-Top10
        BookkeepingStatisticsDto statisticsDto = new BookkeepingStatisticsDto();
        statisticsDto.setRecordCategory(RecordCategoryEnum.INCOME);
        statisticsDto.setCurrentStartMonth(dto.getStartDate());
        statisticsDto.setCurrentEndMonth(dto.getEndDate());
        statisticsDto.setIsSearchAll(dto.getIgnoreNotStatistics());
        List<BookkeepingStatisticsRankVo> rankVoList = getBaseDao().getBaseMapper().rankStatistics(statisticsDto);
        if (CollectionUtils.isNotEmpty(rankVoList)) {
            // 查询记账分类字典值
            List<DictListVo> dictList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
            Map<Integer, String> dictMap = dictList.stream().collect(Collectors.toMap(DictListVo::getDictCode, DictListVo::getDictName));
            List<BookkeepingRecordsIncomeTopVo> topIncomeList = new ArrayList<>();
            for (BookkeepingStatisticsRankVo rankVo : rankVoList) {
                BookkeepingRecordsIncomeTopVo topVo = new BookkeepingRecordsIncomeTopVo();
                topVo.setCategory(dictMap.get(rankVo.getRecordType()));
                topVo.setDescription(rankVo.getRecordSource());
                topVo.setDate(rankVo.getRecordDate());
                topVo.setAmount(rankVo.getAmount());
                topIncomeList.add(topVo);
            }
            incomeVo.setTopIncomeList(topIncomeList);
        }

        // 查询年度收入-收入洞察
        BookkeepingRecordsIncomeInsightsVo insightsVo = new BookkeepingRecordsIncomeInsightsVo();
        BookkeepingRecordsConsumeInsightsVo consumeDaysVo = getBaseDao().getStatisticsMapper().statisticsMaxDay(dto);
        if (consumeDaysVo != null) {
            insightsVo.setMaxDayAmount(consumeDaysVo.getMaxDayAmount());
            insightsVo.setMaxDayDate(consumeDaysVo.getMaxDayDate());
        }
        BookkeepingRecordsConsumeInsightsVo consumeMonthVo = getBaseDao().getStatisticsMapper().statisticsMaxMonth(dto);
        if (consumeMonthVo != null) {
            insightsVo.setMaxMonthAmount(consumeMonthVo.getMaxMonthAmount());
            insightsVo.setMaxMonthName(consumeMonthVo.getMaxMonthName() + "月");
        }
        Long count = getBaseDao().lambdaQuery()
                .between(BookkeepingRecordsEntity::getRecordDate, dto.getStartDate(), dto.getEndDate())
                .eq(dto.getIgnoreNotStatistics() != null && dto.getIgnoreNotStatistics() == 0, BookkeepingRecordsEntity::getIsStatistics, 1)
                .eq(BookkeepingRecordsEntity::getRecordCategory, RecordCategoryEnum.INCOME)
                .ge(BookkeepingRecordsEntity::getAmount, new BigDecimal("100"))
                .count();
        if (count == 0) {
            insightsVo.setLargeIncomeRatio(BigDecimal.ZERO);
        } else {
            insightsVo.setLargeIncomeRatio(new BigDecimal(count * 100).divide(new BigDecimal(incomeVo.getYearStatistics().getIncomeCount()), 2, RoundingMode.HALF_UP));
        }
        insightsVo.setAvgMonthAmount(incomeVo.getYearStatistics().getTotalIncome().divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP));
        incomeVo.setInsights(insightsVo);

        return incomeVo;
    }

    private void computeYearStatisticsParam(BookkeepingRecordsYearStatisticsQueryDto dto) {
        dto.setUserId(UserUtils.getUserId());
        LocalDate startStatisticsDate;
        if (StringUtils.isEmpty(dto.getYear())) {
            startStatisticsDate = DateUtils.startDateOfNowYear();
        } else {
            startStatisticsDate = LocalDate.parse(dto.getYear() + "-01-01", DateUtils.DATE_FORMATTER);
        }
        LocalDate endStatisticsDate = DateUtils.endDateOfYear(startStatisticsDate);
        dto.setStartDate(startStatisticsDate);
        dto.setEndDate(endStatisticsDate);
    }

    private void addPointsRecordsByExcitation(String orderNo) {
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.INCREASE.getCode());
        pointsRecordsAddDto.setPoints(1);
        pointsRecordsAddDto.setSource("记账收入: " + orderNo);
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
        pointsRecordsAddDto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.BOOKKEEPING_SERVICE, pointsRecordsAddDto);
    }

    private void deductPointsRecordsByExcitation(String orderNo) {
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.DEDUCT.getCode());
        pointsRecordsAddDto.setPoints(-1);
        pointsRecordsAddDto.setSource("记账收入被删除: " + orderNo);
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
        pointsRecordsAddDto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.BOOKKEEPING_SERVICE, pointsRecordsAddDto);
    }

    private void syncWalletBalance(RecordCategoryEnum recordCategory, BigDecimal amount) {
        BookkeepingRecordsWalletBalanceDto dto = new BookkeepingRecordsWalletBalanceDto();
        dto.setAmount(RecordCategoryEnum.CONSUME.equals(recordCategory) ? amount.negate() : amount);
        dto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(BookkeepingRecordsTopicEnum.WALLET_AMOUNT, dto);
    }
}
