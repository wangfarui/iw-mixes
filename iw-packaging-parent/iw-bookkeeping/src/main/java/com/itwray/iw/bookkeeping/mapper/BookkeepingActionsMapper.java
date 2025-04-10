package com.itwray.iw.bookkeeping.mapper;

import com.itwray.iw.bookkeeping.model.entity.BookkeepingActionsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 记账行为表 Mapper 接口
 *
 * @author wray
 * @since 2025-04-08
 */
@Mapper
public interface BookkeepingActionsMapper extends BaseMapper<BookkeepingActionsEntity> {

}
