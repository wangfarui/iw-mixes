package com.itwray.iw.points.mapper;

import com.itwray.iw.points.model.bo.ExpiredTaskBo;
import com.itwray.iw.points.model.bo.QueryTaskNumBo;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.points.model.param.QueryExpiredTaskParam;
import com.itwray.iw.points.model.param.QueryGroupTaskNumParam;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 任务基础表 Mapper 接口
 *
 * @author wray
 * @since 2025-03-19
 */
@Mapper
public interface PointsTaskBasicsMapper extends BaseMapper<PointsTaskBasicsEntity> {

    List<QueryTaskNumBo> queryTaskNum(QueryGroupTaskNumParam param);

    @IgnorePermission
    List<ExpiredTaskBo> queryExpiredTask(QueryExpiredTaskParam param);
}
