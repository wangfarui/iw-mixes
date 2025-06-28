package com.itwray.iw.web.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 角色类型枚举
 */
@Getter
public enum RoleTypeEnum implements ConstantEnum {

    USER(1, "普通用户"),
    ADMIN(10, "管理员"),
    SUPER_ADMIN(20, "超级管理员"),
    ;

    private final Integer code;

    private final String name;

    RoleTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否为管理员角色
     *
     * @return true -> 是
     */
    public static boolean isAdminRole(Integer code) {
        return !RoleTypeEnum.USER.getCode().equals(code);
    }
}