package com.itwray.iw.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.entity.PointsTotalEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分合计表 Mapper 接口
 *
 * @author wray
 * @since 2024/9/26
 */
@Mapper
public interface PointsTotalMapper extends BaseMapper<PointsTotalEntity> {
}
