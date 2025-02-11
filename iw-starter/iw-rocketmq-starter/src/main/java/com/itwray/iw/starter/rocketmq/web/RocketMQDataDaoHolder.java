package com.itwray.iw.starter.rocketmq.web;

import com.itwray.iw.starter.rocketmq.web.dao.BaseMqConsumeRecordsDao;
import com.itwray.iw.starter.rocketmq.web.dao.BaseMqProduceRecordsDao;

/**
 * RocketMQ数据持久化对象持有者
 *
 * @author wray
 * @since 2025/2/11
 */
public class RocketMQDataDaoHolder {

    private static BaseMqConsumeRecordsDao baseMqConsumeRecordsDao;

    private static BaseMqProduceRecordsDao baseMqProduceRecordsDao;

    private static String applicationName;

    public static void setBaseMqConsumeRecordsDao(BaseMqConsumeRecordsDao baseMqConsumeRecordsDao) {
        RocketMQDataDaoHolder.baseMqConsumeRecordsDao = baseMqConsumeRecordsDao;
    }

    public static BaseMqConsumeRecordsDao getBaseMqConsumeRecordsDao() {
        return RocketMQDataDaoHolder.baseMqConsumeRecordsDao;
    }

    public static BaseMqProduceRecordsDao getBaseMqProduceRecordsDao() {
        return baseMqProduceRecordsDao;
    }

    public static void setBaseMqProduceRecordsDao(BaseMqProduceRecordsDao baseMqProduceRecordsDao) {
        RocketMQDataDaoHolder.baseMqProduceRecordsDao = baseMqProduceRecordsDao;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static void setApplicationName(String applicationName) {
        RocketMQDataDaoHolder.applicationName = applicationName;
    }
}
