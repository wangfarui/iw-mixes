package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.bookkeeping.dao.BookkeepingIncomesDao;
import com.itwray.iw.bookkeeping.model.dto.IncomesAddDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesPageDto;
import com.itwray.iw.bookkeeping.model.dto.IncomesUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingIncomesEntity;
import com.itwray.iw.bookkeeping.model.vo.IncomesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.IncomesPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingIncomesService;
import com.itwray.iw.web.model.PageVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 收入表 服务实现层
 *
 * @author wray
 * @since 2024/7/15
 */
@Service
public class BookkeepingIncomesServiceImpl implements BookkeepingIncomesService {

    @Resource
    private BookkeepingIncomesDao bookkeepingIncomesDao;

    @Override
    @Transactional
    public void add(IncomesAddDto dto) {
        BookkeepingIncomesEntity incomes = BeanUtil.copyProperties(dto, BookkeepingIncomesEntity.class);
        if (dto.getIncomeDate() == null) {
            incomes.setIncomeDate(LocalDateTime.now());
        }
        bookkeepingIncomesDao.save(incomes);
    }

    @Override
    @Transactional
    public void update(IncomesUpdateDto dto) {
        bookkeepingIncomesDao.queryById(dto.getId());
        BookkeepingIncomesEntity Incomes = BeanUtil.copyProperties(dto, BookkeepingIncomesEntity.class);
        bookkeepingIncomesDao.updateById(Incomes);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        bookkeepingIncomesDao.removeById(id);
    }

    @Override
    public PageVo<IncomesPageVo> page(IncomesPageDto dto) {
        LambdaQueryWrapper<BookkeepingIncomesEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(dto.getIncomeStartDate() != null && dto.getIncomeEndDate() != null,
                        BookkeepingIncomesEntity::getIncomeDate, dto.getIncomeStartDate(), dto.getIncomeEndDate()
                )
                .orderByDesc(BookkeepingIncomesEntity::getId);
        return bookkeepingIncomesDao.page(dto, queryWrapper, IncomesPageVo.class);
    }

    @Override
    public IncomesDetailVo detail(Integer id) {
        BookkeepingIncomesEntity Incomes = bookkeepingIncomesDao.queryById(id);
        return BeanUtil.copyProperties(Incomes, IncomesDetailVo.class);
    }
}
