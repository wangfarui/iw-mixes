package com.itwray.iw.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.entity.PointsTaskRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务执行记录表 Mapper 接口
 *
 * @author wray
 * @since 2025-01-13
 */
@Mapper
public interface PointsTaskRecordsMapper extends BaseMapper<PointsTaskRecordsEntity> {

}
