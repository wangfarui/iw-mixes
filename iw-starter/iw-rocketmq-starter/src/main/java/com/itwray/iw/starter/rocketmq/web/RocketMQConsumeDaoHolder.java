package com.itwray.iw.starter.rocketmq.web;

import com.itwray.iw.starter.rocketmq.web.dao.BaseMqConsumeRecordsDao;

/**
 * RocketMQ消费持久化对象持有者
 *
 * @author wray
 * @since 2025/2/11
 */
public class RocketMQConsumeDaoHolder {

    private static BaseMqConsumeRecordsDao baseMqConsumeRecordsDao;

    private static String applicationName;

    public static void setBaseMqConsumeRecordsDao(BaseMqConsumeRecordsDao baseMqConsumeRecordsDao) {
        RocketMQConsumeDaoHolder.baseMqConsumeRecordsDao = baseMqConsumeRecordsDao;
    }

    public static BaseMqConsumeRecordsDao getBaseMqConsumeRecordsDao() {
        return RocketMQConsumeDaoHolder.baseMqConsumeRecordsDao;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static void setApplicationName(String applicationName) {
        RocketMQConsumeDaoHolder.applicationName = applicationName;
    }
}
