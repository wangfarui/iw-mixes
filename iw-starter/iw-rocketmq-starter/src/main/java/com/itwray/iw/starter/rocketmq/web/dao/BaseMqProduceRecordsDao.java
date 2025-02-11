package com.itwray.iw.starter.rocketmq.web.dao;

import com.itwray.iw.starter.rocketmq.web.entity.BaseMqProduceRecordsEntity;
import com.itwray.iw.starter.rocketmq.web.mapper.BaseMqProduceRecordsMapper;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * MQ消息生产记录表 DAO
 *
 * @author wray
 * @since 2025-02-11
 */
@Component
public class BaseMqProduceRecordsDao extends BaseDao<BaseMqProduceRecordsMapper, BaseMqProduceRecordsEntity> {

}
