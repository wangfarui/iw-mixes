package com.itwray.iw.web.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型枚举
 *
 * @author wray
 * @since 2024/9/10
 */
@Getter
public enum DictTypeEnum implements ConstantEnum {

    /**
     * iw-web web模块
     **/
    WEB_TAG(1001, "web模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),

    /**
     * iw-auth 授权模块
     **/
    AUTH_TAG(2001, "授权模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),
    AUTH_APPLICATION_ACCOUNT_TYPE(2010, "应用账号-应用分类", DataType.CODE, RoleTypeEnum.USER),

    /**
     * iw-eat 餐饮模块
     **/
    EAT_TAG(3001, "餐饮模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),
    EAT_MEAL_TIME(3002, "餐饮-用餐时间", DataType.CODE, RoleTypeEnum.ADMIN),
    EAT_DISHES_TYPE(3003, "餐饮-菜品分类", DataType.CODE, RoleTypeEnum.ADMIN),
    EAT_DISHES_STATUS(3004, "餐饮-菜品状态", DataType.CODE, RoleTypeEnum.ADMIN),

    /**
     * iw-bookkeeping 记账模块
     **/
    BOOKKEEPING_RECORD_TAG(4001, "记账-记录标签", DataType.ID, RoleTypeEnum.USER),
    BOOKKEEPING_RECORD_TYPE(4002, "记账-记录分类", DataType.CODE, RoleTypeEnum.USER),
    BOOKKEEPING_RECORD_CATEGORY(4003, "记账-记录类型", DataType.CODE, RoleTypeEnum.ADMIN),
    ;

    private final Integer code;

    private final String name;

    private final DataType dataType;

    private final RoleTypeEnum roleTypeEnum;

    DictTypeEnum(Integer code, String name, DataType dataType, RoleTypeEnum roleTypeEnum) {
        this.code = code;
        this.name = name;
        this.dataType = dataType;
        this.roleTypeEnum = roleTypeEnum;
    }

    /**
     * 是否为管理员字典
     *
     * @return true -> 是
     */
    public boolean isAdminDict() {
        return !RoleTypeEnum.USER.equals(this.getRoleTypeEnum());
    }

    public enum DataType {
        ID,
        CODE
    }

    /**
     * 获取user可管理的字典
     */
    public static List<DictTypeEnum> getUserDict() {
        return Arrays.stream(DictTypeEnum.values()).filter(t -> RoleTypeEnum.USER.equals(t.getRoleTypeEnum())).collect(Collectors.toList());
    }

    /**
     * 获取admin可管理的字典
     */
    public static List<DictTypeEnum> getAdminDict() {
        return Arrays.stream(DictTypeEnum.values())
                .filter(t -> !RoleTypeEnum.SUPER_ADMIN.equals(t.getRoleTypeEnum()))
                .collect(Collectors.toList());
    }
}
