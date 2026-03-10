package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 家庭成员状态枚举
 *
 * @author wray
 * @since 2024-03-10
 */
@Getter
@AllArgsConstructor
public enum FamilyMemberStatusEnum implements BusinessConstantEnum {

    /**
     * 正常
     */
    NORMAL(1, "正常"),

    /**
     * 已退出
     */
    QUIT(2, "已退出"),

    /**
     * 已移除
     */
    REMOVED(3, "已移除");

    private final Integer code;
    private final String name;
}
