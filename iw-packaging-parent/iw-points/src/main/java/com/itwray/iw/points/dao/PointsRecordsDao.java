package com.itwray.iw.points.dao;

import com.itwray.iw.points.mapper.PointsRecordsMapper;
import com.itwray.iw.points.model.entity.PointsRecordsEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 积分记录表 DAO
 *
 * @author wray
 * @since 2024/9/26
 */
@Component
public class PointsRecordsDao extends BaseDao<PointsRecordsMapper, PointsRecordsEntity> {
}
