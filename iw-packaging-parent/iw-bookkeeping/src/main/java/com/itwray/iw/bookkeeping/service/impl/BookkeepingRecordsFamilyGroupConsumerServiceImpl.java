package com.itwray.iw.bookkeeping.service.impl;

import com.itwray.iw.auth.model.enums.ShareStateEnum;
import com.itwray.iw.auth.model.mq.FamilyGroupMemberLeaveMqDto;
import com.itwray.iw.bookkeeping.dao.BookkeepingRecordsDao;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingRecordsEntity;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.model.enums.mq.FamilyGroupTopicEnum;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 记账记录-家庭组成员离组消费者
 *
 * @author wray
 * @since 2026/3/12
 */
@Service
@RocketMQMessageListener(consumerGroup = "bookkeeping-records-family-group-service", topic = FamilyGroupTopicEnum.TOPIC, tag = "member_leave")
public class BookkeepingRecordsFamilyGroupConsumerServiceImpl implements RocketMQClientListener<FamilyGroupMemberLeaveMqDto> {

    private final BookkeepingRecordsDao bookkeepingRecordsDao;

    @Autowired
    public BookkeepingRecordsFamilyGroupConsumerServiceImpl(BookkeepingRecordsDao bookkeepingRecordsDao) {
        this.bookkeepingRecordsDao = bookkeepingRecordsDao;
    }

    @Override
    public Class<FamilyGroupMemberLeaveMqDto> getGenericClass() {
        return FamilyGroupMemberLeaveMqDto.class;
    }

    @Override
    public void doConsume(FamilyGroupMemberLeaveMqDto dto) {
        if (dto == null || dto.getGroupId() == null || dto.getGroupId() <= 0 || CollectionUtils.isEmpty(dto.getUserIdList())) {
            return;
        }
        try {
            UserUtils.setUserDataPermission(false);
            bookkeepingRecordsDao.lambdaUpdate()
                    .eq(BookkeepingRecordsEntity::getGroupId, dto.getGroupId())
                    .in(BookkeepingRecordsEntity::getUserId, dto.getUserIdList())
                    .eq(BookkeepingRecordsEntity::getShareState, ShareStateEnum.SHARED)
                    .set(BookkeepingRecordsEntity::getShareState, ShareStateEnum.LEFT_GROUP)
                    .update();
        } finally {
            UserUtils.removeUserDataPermission();
        }
    }
}
