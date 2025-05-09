package com.itwray.iw.points.mapper;

import com.itwray.iw.points.model.entity.PointsTaskPlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.param.QueryPlanTaskParam;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 任务计划表 Mapper 接口
 *
 * @author wray
 * @since 2025-05-07
 */
@Mapper
public interface PointsTaskPlanMapper extends BaseMapper<PointsTaskPlanEntity> {

    @IgnorePermission
    List<PointsTaskPlanEntity> queryPlanTask(QueryPlanTaskParam param);
}
