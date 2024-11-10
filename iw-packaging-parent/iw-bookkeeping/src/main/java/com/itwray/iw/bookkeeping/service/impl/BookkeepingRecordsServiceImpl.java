package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.mapper.BookkeepingRecordsMapper;
import com.itwray.iw.bookkeeping.model.bo.RecordsStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordsStatisticsVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.common.constants.BoolEnums;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.constants.MQTopicConstants;
import com.itwray.iw.web.dao.BaseDictBusinessRelationDao;
import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class BookkeepingRecordsServiceImpl extends WebServiceImpl<BookkeepingRecordsMapper, BookkeepingRecordsEntity,
        BookkeepingRecordsDao, BookkeepingRecordDetailVo> implements BookkeepingRecordsService {

    private final BaseDictBusinessRelationDao baseDictBusinessRelationDao;

    @Autowired
    public BookkeepingRecordsServiceImpl(BookkeepingRecordsDao baseDao,
                                         BaseDictBusinessRelationDao baseDictBusinessRelationDao) {
        super(baseDao);
        this.baseDictBusinessRelationDao = baseDictBusinessRelationDao;
    }

    @Override
    @Transactional
    public Serializable add(AddDto dto) {
        BookkeepingRecordsEntity bookkeepingRecords = BeanUtil.copyProperties(dto, BookkeepingRecordsEntity.class);
        // 记录日期为空是默认取当前时间
        if (bookkeepingRecords.getRecordDate() == null) {
            bookkeepingRecords.setRecordDate(LocalDate.now());
            bookkeepingRecords.setRecordTime(LocalDateTime.now());
        } else {
            // 日期取指定日期，时间取当前时间
            bookkeepingRecords.setRecordTime(bookkeepingRecords.getRecordDate().atTime(LocalTime.now()));
        }
        getBaseDao().save(bookkeepingRecords);

        // 保存标签
        if (dto instanceof BookkeepingRecordAddDto recordAddDto) {
            baseDictBusinessRelationDao.saveRelation(bookkeepingRecords.getId(), recordAddDto.getRecordTags());
            // 记录为激励收入时，积分+1
            if (RecordCategoryEnum.INCOME.getCode().equals(recordAddDto.getRecordCategory())
                    && BoolEnums.TRUE.getCode().equals(recordAddDto.getIsExcitationRecord())) {
                PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
                pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.INCREASE.getCode());
                pointsRecordsAddDto.setPoints(1);
                pointsRecordsAddDto.setSource("记账收入");
                pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
                pointsRecordsAddDto.setUserId(UserUtils.getUserId());
                MQProducerHelper.send(MQTopicConstants.POINTS_RECORDS, pointsRecordsAddDto);
            }
        }

        return bookkeepingRecords.getId();
    }

    @Override
    @Transactional
    public void delete(Serializable id) {
        BookkeepingRecordsEntity bookkeepingRecordsEntity = getBaseDao().queryById(id);
        super.delete(id);
        // 删除标签
        baseDictBusinessRelationDao.removeRelation(id);
        // 同步积分数据
        if (RecordCategoryEnum.INCOME.getCode().equals(bookkeepingRecordsEntity.getRecordCategory())
                && BoolEnums.TRUE.getCode().equals(bookkeepingRecordsEntity.getIsExcitationRecord())) {
            PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
            pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.DEDUCT.getCode());
            pointsRecordsAddDto.setPoints(-1);
            pointsRecordsAddDto.setSource("记账收入被删除");
            pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.BOOKKEEPING.getCode());
            pointsRecordsAddDto.setUserId(bookkeepingRecordsEntity.getUserId());
            MQProducerHelper.send(MQTopicConstants.POINTS_RECORDS, pointsRecordsAddDto);
        }
    }

    @Override
    public BookkeepingRecordDetailVo detail(Serializable id) {
        BookkeepingRecordDetailVo vo = super.detail(id);

        // 查询标签
        List<Integer> tagIdList = baseDictBusinessRelationDao.queryDictIdList(id);
        vo.setRecordTags(tagIdList);

        return vo;
    }

    @Override
    public PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto) {
        LambdaQueryWrapper<BookkeepingRecordsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(dto.getRecordStartDate() != null && dto.getRecordEndDate() != null,
                        BookkeepingRecordsEntity::getRecordDate, dto.getRecordStartDate(), dto.getRecordEndDate()
                )
                .orderByDesc(BookkeepingRecordsEntity::getId);
        return getBaseDao().page(dto, queryWrapper, BookkeepingRecordPageVo.class);
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
}
