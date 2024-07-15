package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.bookkeeping.dao.BookkeepingExpensesDao;
import com.itwray.iw.bookkeeping.model.dto.ExpensesAddDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesPageDto;
import com.itwray.iw.bookkeeping.model.dto.ExpensesUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingExpensesEntity;
import com.itwray.iw.bookkeeping.model.vo.ExpensesDetailVo;
import com.itwray.iw.bookkeeping.model.vo.ExpensesPageVo;
import com.itwray.iw.bookkeeping.service.BookkeepingExpensesService;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.web.model.PageVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 支出表 服务实现层
 *
 * @author wray
 * @since 2024/7/15
 */
@Service
public class BookkeepingExpensesServiceImpl implements BookkeepingExpensesService {

    @Resource
    private BookkeepingExpensesDao bookkeepingExpensesDao;

    @Override
    @Transactional
    public void add(ExpensesAddDto dto) {
        BookkeepingExpensesEntity expenses = BeanUtil.copyProperties(dto, BookkeepingExpensesEntity.class);
        if (dto.getExpenseDate() == null) {
            expenses.setExpenseDate(LocalDateTime.now());
        }
        bookkeepingExpensesDao.save(expenses);
    }

    @Override
    @Transactional
    public void update(ExpensesUpdateDto dto) {
        bookkeepingExpensesDao.queryById(dto.getId());
        BookkeepingExpensesEntity expenses = BeanUtil.copyProperties(dto, BookkeepingExpensesEntity.class);
        bookkeepingExpensesDao.updateById(expenses);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        bookkeepingExpensesDao.removeById(id);
    }

    @Override
    public PageVo<ExpensesPageVo> page(ExpensesPageDto dto) {
        LambdaQueryWrapper<BookkeepingExpensesEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(dto.getExpenseStartDate() != null && dto.getExpenseEndDate() != null,
                        BookkeepingExpensesEntity::getExpenseDate, dto.getExpenseStartDate(), dto.getExpenseEndDate()
                )
                .orderByDesc(BookkeepingExpensesEntity::getId);
        return bookkeepingExpensesDao.page(dto, queryWrapper, ExpensesPageVo.class);
    }

    @Override
    public ExpensesDetailVo detail(Integer id) {
        BookkeepingExpensesEntity expenses = bookkeepingExpensesDao.queryById(id);
        return BeanUtil.copyProperties(expenses, ExpensesDetailVo.class);
    }
}
