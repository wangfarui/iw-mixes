package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 家庭邀请状态枚举
 *
 * @author wray
 * @since 2024-03-10
 */
@Getter
@AllArgsConstructor
public enum FamilyInviteStatusEnum implements BusinessConstantEnum {

    /**
     * 待使用
     */
    PENDING(1, "待使用"),

    /**
     * 已使用
     */
    ACCEPTED(2, "已使用"),

    /**
     * 已过期
     */
    EXPIRED(4, "已过期");

    private final Integer code;
    private final String name;
}
