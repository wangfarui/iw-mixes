package com.itwray.iw.external.dao;

import com.itwray.iw.external.mapper.SmsRecordsMapper;
import com.itwray.iw.external.model.entity.SmsRecordsEntity;
import com.itwray.iw.external.model.enums.SmsSendStatusEnum;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * 外部SMS短信记录表 DAO
 *
 * @author wray
 * @since 2024-12-24
 */
@Component
public class SmsRecordsDao extends BaseDao<SmsRecordsMapper, SmsRecordsEntity> {

    /**
     * 更新短信记录的发送状态
     *
     * @param id                短信记录id
     * @param smsSendStatusEnum 发送状态枚举
     */
    public void updateSendStatus(Integer id, SmsSendStatusEnum smsSendStatusEnum) {
        this.lambdaUpdate()
                .eq(SmsRecordsEntity::getId, id)
                .set(SmsRecordsEntity::getSendStatus, smsSendStatusEnum.getCode())
                .update(new SmsRecordsEntity());
    }
}
