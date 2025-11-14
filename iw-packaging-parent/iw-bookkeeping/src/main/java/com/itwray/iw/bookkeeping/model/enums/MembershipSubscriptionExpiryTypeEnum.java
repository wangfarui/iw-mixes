package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 会员订阅有效期类型枚举
 *
 * @author farui.wang
 * @since 2025/5/6
 */
@Getter
public enum MembershipSubscriptionExpiryTypeEnum implements BusinessConstantEnum {

    ALL(0, "默认"),
    VALID(1, "有效期内"),
    ABOUT_EXPIRE(2, "即将到期"),
    EXPIRED(3, "已过期");

    private final Integer code;

    private final String name;

    MembershipSubscriptionExpiryTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
