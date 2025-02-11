package com.itwray.iw.starter.rocketmq.web.dao;

import com.itwray.iw.starter.rocketmq.web.entity.BaseMqConsumeRecordsEntity;
import com.itwray.iw.starter.rocketmq.web.mapper.BaseMqConsumeRecordsMapper;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * MQ消息消费记录表 DAO
 *
 * @author wray
 * @since 2025-02-10
 */
@Component
public class BaseMqConsumeRecordsDao extends BaseDao<BaseMqConsumeRecordsMapper, BaseMqConsumeRecordsEntity> {

}
