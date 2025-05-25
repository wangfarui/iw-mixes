package com.itwray.iw.points.dao;

import com.itwray.iw.points.model.entity.PointsTaskRelationEntity;
import com.itwray.iw.points.mapper.PointsTaskRelationMapper;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 任务关联表 DAO
 *
 * @author wray
 * @since 2025-04-17
 */
@Component
public class PointsTaskRelationDao extends BaseDao<PointsTaskRelationMapper, PointsTaskRelationEntity> {

    public PointsTaskRelationEntity getByTaskId(Integer taskId) {
        return this.lambdaQuery()
                .eq(PointsTaskRelationEntity::getTaskId, taskId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
    }
}
