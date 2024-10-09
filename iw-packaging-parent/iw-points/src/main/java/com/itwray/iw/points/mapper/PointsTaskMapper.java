package com.itwray.iw.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.entity.PointsTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分任务表 Mapper 接口
 *
 * @author wray
 * @since 2024/9/26
 */
@Mapper
public interface PointsTaskMapper extends BaseMapper<PointsTaskEntity> {
}
