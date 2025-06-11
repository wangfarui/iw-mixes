package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.idev.excel.FastExcel;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.excel.listener.BookkeepingRecordsImportDataListener;
import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsMapper;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingRecordsImportBo;
import com.itwray.iw.bookkeeping.model.bo.RecordsStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.*;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingRecordTypeDefaultEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordsStatisticsVo;
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
import java.util.List;
import java.util.Map;
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

    private final InternalApiClient internalApiClient;

    private final BaseBusinessFileDao baseBusinessFileDao;

    private BaseDictDao baseDictDao;

    @SuppressWarnings("all")
    @Autowired
    public BookkeepingRecordsServiceImpl(BookkeepingRecordsDao baseDao,
                                         BaseDictBusinessRelationDao baseDictBusinessRelationDao,
                                         InternalApiClient internalApiClient,
                                         BaseBusinessFileDao baseBusinessFileDao) {
        super(baseDao);
        this.baseDictBusinessRelationDao = baseDictBusinessRelationDao;
        this.internalApiClient = internalApiClient;
        this.baseBusinessFileDao = baseBusinessFileDao;
    }

    @Autowired
    public void setBaseDictDao(BaseDictDao baseDictDao) {
        this.baseDictDao = baseDictDao;
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
        this.syncWalletBalance(dto.getRecordCategory(), dto.getAmount());

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
        if (bookkeepingRecordsEntity.getAmount().compareTo(dto.getAmount()) != 0) {
            this.syncWalletBalance(dto.getRecordCategory(), dto.getAmount().subtract(bookkeepingRecordsEntity.getAmount()));
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
        if (CollUtil.isNotEmpty(dto.getTagIdList())) {
            dto.setTagBusinessType(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG.getCode());
        }
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

    @Override
    public BookkeepingRecordsStatisticsVo statistics(BookkeepingRecordsStatisticsDto dto) {
        if (dto.getRecordStartDate() == null) {
            dto.setRecordStartDate(DateUtils.startDateOfNowMonth());
        }
        if (dto.getRecordEndDate() == null) {
            dto.setRecordEndDate(DateUtils.endDateOfNowMonth());
        }
        if (CollUtil.isNotEmpty(dto.getTagIdList())) {
            dto.setTagBusinessType(DictBusinessTypeEnum.BOOKKEEPING_RECORD_TAG.getCode());
        }

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
        } else if (bo.getRemark().length() < 50){
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

    private void addPointsRecordsByExcitation(String orderNo) {
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.INCREASE.getCode());
        pointsRecordsAddDto.setPoints(1);
        pointsRecordsAddDto.setSource("记账收入: " + orderNo);
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
        pointsRecordsAddDto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.EXCITATION_BOOKKEEPING, pointsRecordsAddDto);
    }

    private void deductPointsRecordsByExcitation(String orderNo) {
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.DEDUCT.getCode());
        pointsRecordsAddDto.setPoints(-1);
        pointsRecordsAddDto.setSource("记账收入被删除: " + orderNo);
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
        pointsRecordsAddDto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.EXCITATION_BOOKKEEPING, pointsRecordsAddDto);
    }

    private void syncWalletBalance(RecordCategoryEnum recordCategory, BigDecimal amount) {
        BookkeepingRecordsWalletBalanceDto dto = new BookkeepingRecordsWalletBalanceDto();
        dto.setAmount(RecordCategoryEnum.CONSUME.equals(recordCategory) ? amount.negate() : amount);
        dto.setUserId(UserUtils.getUserId());
        MQProducerHelper.send(BookkeepingRecordsTopicEnum.WALLET_AMOUNT, dto);
    }
}
