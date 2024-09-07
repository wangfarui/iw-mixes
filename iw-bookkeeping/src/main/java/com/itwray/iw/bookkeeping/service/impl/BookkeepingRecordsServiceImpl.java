package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.web.model.PageVo;
import com.itwray.iw.web.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 记录表 服务实现层
 *
 * @author wray
 * @since 2024/8/28
 */
@Service
public class BookkeepingRecordsServiceImpl implements BookkeepingRecordsService {

    @Resource
    private BookkeepingRecordsDao bookkeepingRecordsDao;

    @Override
    @Transactional
    public void add(BookkeepingRecordAddDto dto) {
        BookkeepingRecordsEntity bookkeepingRecords = BeanUtil.copyProperties(dto, BookkeepingRecordsEntity.class);
        // 记录日期为空是默认取当前时间
        if (dto.getRecordDate() == null) {
            bookkeepingRecords.setRecordDate(LocalDate.now());
            bookkeepingRecords.setRecordTime(LocalDateTime.now());
        } else {
            // 日期取指定日期，时间取当前时间
            bookkeepingRecords.setRecordTime(dto.getRecordDate().atTime(LocalTime.now()));
        }
        bookkeepingRecords.setUserId(UserUtils.getUserId());
        bookkeepingRecordsDao.save(bookkeepingRecords);
    }

    @Override
    @Transactional
    public void update(BookkeepingRecordUpdateDto dto) {
        bookkeepingRecordsDao.queryById(dto.getId());
        BookkeepingRecordsEntity bookkeepingRecords = BeanUtil.copyProperties(dto, BookkeepingRecordsEntity.class);
        bookkeepingRecordsDao.updateById(bookkeepingRecords);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        bookkeepingRecordsDao.removeById(id);
    }

    @Override
    public PageVo<BookkeepingRecordPageVo> page(BookkeepingRecordPageDto dto) {
        LambdaQueryWrapper<BookkeepingRecordsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(dto.getRecordStartDate() != null && dto.getRecordEndDate() != null,
                        BookkeepingRecordsEntity::getRecordDate, dto.getRecordStartDate(), dto.getRecordEndDate()
                )
                .orderByDesc(BookkeepingRecordsEntity::getId);
        return bookkeepingRecordsDao.page(dto, queryWrapper, BookkeepingRecordPageVo.class);
    }

    @Override
    public BookkeepingRecordDetailVo detail(Integer id) {
        BookkeepingRecordsEntity Incomes = bookkeepingRecordsDao.queryById(id);
        return BeanUtil.copyProperties(Incomes, BookkeepingRecordDetailVo.class);
    }

    @Override
    public List<BookkeepingRecordPageVo> list(BookkeepingRecordListDto dto) {
        // 记账日期为空时，默认查当天
        if (dto.getRecordDate() == null) {
            dto.setRecordDate(LocalDate.now());
        }
        return bookkeepingRecordsDao.lambdaQuery()
                .eq(BookkeepingRecordsEntity::getRecordDate, dto.getRecordDate())
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, BookkeepingRecordPageVo.class))
                .collect(Collectors.toList());
    }
}
