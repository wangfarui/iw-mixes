package com.itwray.iw.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.bo.PointsRecordsStatisticsBo;
import com.itwray.iw.points.model.dto.PointsRecordsStatisticsDto;
import com.itwray.iw.points.model.entity.PointsRecordsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分记录表 Mapper 接口
 *
 * @author wray
 * @since 2024/9/26
 */
@Mapper
public interface PointsRecordsMapper extends BaseMapper<PointsRecordsEntity> {

    List<PointsRecordsStatisticsBo> statistics(@Param("dto") PointsRecordsStatisticsDto dto);
}
