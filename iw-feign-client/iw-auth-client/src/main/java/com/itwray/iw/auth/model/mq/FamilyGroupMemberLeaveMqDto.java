package com.itwray.iw.auth.model.mq;

import lombok.Data;

import java.util.List;

/**
 * 家庭组成员离组消息
 *
 * @author wray
 * @since 2026/3/12
 */
@Data
public class FamilyGroupMemberLeaveMqDto {

    /**
     * 家庭组ID
     */
    private Integer groupId;

    /**
     * 离组用户ID列表
     */
    private List<Integer> userIdList;
}
