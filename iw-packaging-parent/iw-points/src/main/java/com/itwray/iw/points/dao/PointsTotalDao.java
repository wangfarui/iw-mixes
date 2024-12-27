package com.itwray.iw.points.dao;

import com.itwray.iw.points.mapper.PointsTotalMapper;
import com.itwray.iw.points.model.entity.PointsTotalEntity;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 积分合计表 DAO
 *
 * @author wray
 * @since 2024/9/26
 */
@Component
public class PointsTotalDao extends BaseDao<PointsTotalMapper, PointsTotalEntity> {

    /**
     * 更新积分余额
     *
     * @param points 积分变动数量(可以是正数或负数)
     */
    @Transactional
    public void updatePointsBalance(Integer points) {
        this.lambdaUpdate()
                .eq(PointsTotalEntity::getUserId, UserUtils.getUserId())
                .setSql("points_balance = points_balance + " + points)
                .update(new PointsTotalEntity());
    }
}
