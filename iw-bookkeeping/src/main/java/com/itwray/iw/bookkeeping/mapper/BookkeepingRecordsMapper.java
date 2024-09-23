package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.bookkeeping.model.bo.RecordsStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 记账记录表 Mapper接口
 *
 * @author wray
 * @since 2024/8/28
 */
@Mapper
public interface BookkeepingRecordsMapper extends BaseMapper<BookkeepingRecordsEntity> {

    List<RecordsStatisticsBo> statistics(@Param("dto") BookkeepingRecordsStatisticsDto dto);
}
