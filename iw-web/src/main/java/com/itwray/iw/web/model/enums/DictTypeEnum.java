package com.itwray.iw.web.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 字典类型枚举
 *
 * @author wray
 * @since 2024/9/10
 */
@Getter
public enum DictTypeEnum implements ConstantEnum {

    /** iw-web web模块 **/
    WEB_TAG(1001, "web模块", DataType.ID),

    /** iw-auth 授权模块 **/
    AUTH_TAG(2001, "授权模块", DataType.ID),
    AUTH_APPLICATION_ACCOUNT_TYPE(2010, "应用账号-应用分类", DataType.CODE),

    /** iw-eat 餐饮模块 **/
    EAT_TAG(3001, "餐饮模块", DataType.ID),
    EAT_MEAL_TIME(3002, "餐饮-用餐时间", DataType.CODE),
    EAT_DISHES_TYPE(3003, "餐饮-菜品分类", DataType.CODE),
    EAT_DISHES_STATUS(3004, "餐饮-菜品状态", DataType.CODE),

    /** iw-bookkeeping 记账模块 **/
    BOOKKEEPING_RECORD_TAG(4001, "记账-记录标签", DataType.ID),
    BOOKKEEPING_RECORD_TYPE(4002, "记账-记录分类", DataType.CODE),
    BOOKKEEPING_RECORD_CATEGORY(4003, "记账-记录类型", DataType.CODE),
    ;

    private final Integer code;

    private final String name;

    private final DataType dataType;

    DictTypeEnum(Integer code, String name, DataType dataType) {
        this.code = code;
        this.name = name;
        this.dataType = dataType;
    }

    public enum DataType {
        ID,
        CODE
    }
}
