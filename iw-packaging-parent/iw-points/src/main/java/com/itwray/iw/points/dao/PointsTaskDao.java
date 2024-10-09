package com.itwray.iw.points.dao;

import com.itwray.iw.points.mapper.PointsTaskMapper;
import com.itwray.iw.points.model.entity.PointsTaskEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 积分任务表 DAO
 *
 * @author wray
 * @since 2024/9/26
 */
@Component
public class PointsTaskDao extends BaseDao<PointsTaskMapper, PointsTaskEntity> {

}
