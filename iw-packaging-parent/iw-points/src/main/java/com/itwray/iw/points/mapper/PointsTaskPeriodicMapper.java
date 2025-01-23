package com.itwray.iw.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.bo.PointsTaskFullBo;
import com.itwray.iw.points.model.dto.PointsTaskConditionDto;
import com.itwray.iw.points.model.entity.PointsTaskPeriodicEntity;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 周期性任务表 Mapper 接口
 *
 * @author wray
 * @since 2025-01-13
 */
@Mapper
public interface PointsTaskPeriodicMapper extends BaseMapper<PointsTaskPeriodicEntity> {

    @IgnorePermission
    List<PointsTaskFullBo> queryClockInTask(@Param("dto") PointsTaskConditionDto dto);
}
