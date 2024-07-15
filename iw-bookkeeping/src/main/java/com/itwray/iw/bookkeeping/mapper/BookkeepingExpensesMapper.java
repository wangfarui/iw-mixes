package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingExpensesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支出表 Mapper接口
 *
 * @author wray
 * @since 2024/7/15
 */
@Mapper
public interface BookkeepingExpensesMapper extends BaseMapper<BookkeepingExpensesEntity> {
}
