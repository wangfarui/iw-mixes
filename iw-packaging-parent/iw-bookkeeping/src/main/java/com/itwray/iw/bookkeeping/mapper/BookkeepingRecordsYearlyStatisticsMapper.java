package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingRecordsYearStatisticsQueryDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.bookkeeping.model.vo.yearly.consume.BookkeepingRecordsConsumeInsightsVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.consume.BookkeepingRecordsConsumeTagsVo;
import com.itwray.iw.bookkeeping.model.vo.yearly.overview.BookkeepingRecordsOverviewHabitsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 记账记录表-年度统计 Mapper接口
 *
 * @author wray
 * @since 2025/12/11
 */
@Mapper
public interface BookkeepingRecordsYearlyStatisticsMapper extends BaseMapper<BookkeepingRecordsEntity> {

    BookkeepingRecordsOverviewHabitsVo statisticsMaxContinuousDays(BookkeepingRecordsYearStatisticsQueryDto dto);

    Integer statisticsRecordingDays(BookkeepingRecordsYearStatisticsQueryDto dto);

    BookkeepingRecordsOverviewHabitsVo statisticsPeakMonth(BookkeepingRecordsYearStatisticsQueryDto dto);

    Integer statisticsMissingCount(BookkeepingRecordsYearStatisticsQueryDto dto);

    List<BookkeepingRecordsConsumeTagsVo> statisticsTagConsume(BookkeepingRecordsYearStatisticsQueryDto dto);

    BookkeepingRecordsConsumeInsightsVo statisticsMaxDay(BookkeepingRecordsYearStatisticsQueryDto dto);

    BookkeepingRecordsConsumeInsightsVo statisticsMaxMonth(BookkeepingRecordsYearStatisticsQueryDto dto);
}
