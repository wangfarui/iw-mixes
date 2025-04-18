package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.points.service.PointsTaskRelationService;
import com.itwray.iw.starter.redis.lock.DistributedLock;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.points.model.dto.PointsTaskRelationAddDto;
import com.itwray.iw.points.model.dto.PointsTaskRelationUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskRelationDetailVo;
import com.itwray.iw.points.dao.PointsTaskRelationDao;
import com.itwray.iw.points.mapper.PointsTaskRelationMapper;
import com.itwray.iw.points.model.entity.PointsTaskRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务关联表 服务实现类
 *
 * @author wray
 * @since 2025-04-17
 */
@Service
public class PointsTaskRelationServiceImpl extends WebServiceImpl<PointsTaskRelationDao, PointsTaskRelationMapper, PointsTaskRelationEntity,
        PointsTaskRelationAddDto, PointsTaskRelationUpdateDto, PointsTaskRelationDetailVo, Integer>  implements PointsTaskRelationService {

    @Autowired
    public PointsTaskRelationServiceImpl(PointsTaskRelationDao baseDao) {
        super(baseDao);
    }

    @Override
    @Transactional
    @DistributedLock(lockName = "'PointsTaskRelation-Save:' + #dto.taskId")
    public Integer save(PointsTaskRelationAddDto dto) {
        PointsTaskRelationEntity pointsTaskRelationEntity = getBaseDao().getByTaskId(dto.getTaskId());
        if (pointsTaskRelationEntity != null) {
            pointsTaskRelationEntity.setPunishPoints(dto.getPunishPoints());
            pointsTaskRelationEntity.setRewardPoints(dto.getRewardPoints());
        } else {
            pointsTaskRelationEntity = BeanUtil.copyProperties(dto, PointsTaskRelationEntity.class);
        }
        if (pointsTaskRelationEntity.getRewardPoints() == null) {
            pointsTaskRelationEntity.setRewardPoints(WebCommonConstants.DATABASE_DEFAULT_INT_VALUE);
        }
        if (pointsTaskRelationEntity.getPunishPoints() == null) {
            pointsTaskRelationEntity.setPunishPoints(WebCommonConstants.DATABASE_DEFAULT_INT_VALUE);
        }
        getBaseDao().saveOrUpdate(pointsTaskRelationEntity);

        return pointsTaskRelationEntity.getId();
    }

    @Override
    public PointsTaskRelationDetailVo getByTaskId(Integer taskId) {
        PointsTaskRelationEntity pointsTaskRelationEntity = getBaseDao().getByTaskId(taskId);
        return BeanUtil.copyProperties(pointsTaskRelationEntity, PointsTaskRelationDetailVo.class);
    }
}
