package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingBarChartStatisticsBo;
import com.itwray.iw.bookkeeping.model.bo.RecordsStatisticsBo;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingConsumeCategoryStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordPageDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsStatisticsDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingStatisticsDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingConsumeStatisticsCategoryVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingRecordPageVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsRankVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingStatisticsTotalVo;
import com.itwray.iw.web.model.vo.PageVo;
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

    PageVo<BookkeepingRecordPageVo> page(IPage<BookkeepingRecordPageVo> page, @Param("dto") BookkeepingRecordPageDto dto);

    List<RecordsStatisticsBo> statistics(@Param("dto") BookkeepingRecordsStatisticsDto dto);

    BookkeepingStatisticsTotalVo totalStatistics(@Param("dto") BookkeepingStatisticsDto dto);

    List<BookkeepingStatisticsRankVo> rankStatistics(@Param("dto") BookkeepingStatisticsDto dto);

    List<BookkeepingBarChartStatisticsBo> barChartStatistics(@Param("dto") BookkeepingStatisticsDto dto);

    List<BookkeepingConsumeStatisticsCategoryVo> categoryStatistics(@Param("dto") BookkeepingConsumeCategoryStatisticsDto dto);
}
