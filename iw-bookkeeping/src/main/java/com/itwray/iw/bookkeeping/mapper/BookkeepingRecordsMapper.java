package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 记账记录表 Mapper接口
 *
 * @author wray
 * @since 2024/8/28
 */
@Mapper
public interface BookkeepingRecordsMapper extends BaseMapper<BookkeepingRecordsEntity> {
}
