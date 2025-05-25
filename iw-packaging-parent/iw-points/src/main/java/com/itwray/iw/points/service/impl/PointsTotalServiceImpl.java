package com.itwray.iw.points.service.impl;

import com.itwray.iw.auth.model.bo.UserAddBo;
import com.itwray.iw.points.dao.PointsTotalDao;
import com.itwray.iw.points.model.entity.PointsTotalEntity;
import com.itwray.iw.points.service.PointsTotalService;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.model.enums.mq.RegisterNewUserTopicEnum;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
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
@Slf4j
@RocketMQMessageListener(consumerGroup = "points-total-service", topic = RegisterNewUserTopicEnum.TOPIC, tag = "init")
public class PointsTotalServiceImpl implements PointsTotalService, RocketMQClientListener<UserAddBo> {

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

    @Override
    public Class<UserAddBo> getGenericClass() {
        return UserAddBo.class;
    }

    @Override
    public void doConsume(UserAddBo bo) {
        // 判断该用户是否已生成过积分合计表
        PointsTotalEntity oldEntity = pointsTotalDao.lambdaQuery()
                .eq(PointsTotalEntity::getUserId, bo.getUserId())
                .select(PointsTotalEntity::getId)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (oldEntity != null) {
            log.info("用户[{}]已存在积分合计表数据, 默认跳过初始化积分合计表数据操作", bo.getUserId());
            return;
        }

        PointsTotalEntity entity = new PointsTotalEntity();
        entity.setPointsBalance(0);
        entity.setUserId(bo.getUserId());
        pointsTotalDao.save(entity);

        log.info("用户[{}]积分合计表初始化完成", bo.getUserId());
    }
}
