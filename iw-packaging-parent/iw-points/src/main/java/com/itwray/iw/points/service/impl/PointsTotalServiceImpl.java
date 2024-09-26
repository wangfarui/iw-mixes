package com.itwray.iw.points.service.impl;

import com.itwray.iw.points.dao.PointsTotalDao;
import com.itwray.iw.points.model.entity.PointsTotalEntity;
import com.itwray.iw.points.service.PointsTotalService;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 积分合计表 服务实现层
 *
 * @author wray
 * @since 2024/9/26
 */
@Service
public class PointsTotalServiceImpl implements PointsTotalService {

    private final PointsTotalDao pointsTotalDao;

    @Autowired
    public PointsTotalServiceImpl(PointsTotalDao pointsTotalDao) {
        this.pointsTotalDao = pointsTotalDao;
    }

    @Override
    public Integer getPointsBalance() {
        PointsTotalEntity pointsTotalEntity = pointsTotalDao.lambdaQuery()
                .eq(PointsTotalEntity::getUserId, UserUtils.getUserId())
                .select(PointsTotalEntity::getPointsBalance)
                .one();
        return Optional.ofNullable(pointsTotalEntity).map(PointsTotalEntity::getPointsBalance).orElse(0);
    }
}
