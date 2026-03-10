package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 家庭成员角色枚举
 *
 * @author wray
 * @since 2024-03-10
 */
@Getter
@AllArgsConstructor
public enum FamilyMemberRoleEnum implements BusinessConstantEnum {

    /**
     * 群主
     */
    OWNER(1, "群主"),

    /**
     * 成员
     */
    MEMBER(2, "成员");

    private final Integer code;
    private final String name;
}
