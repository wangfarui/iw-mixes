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

    /** iw-web web模块 **/
    WEB_TAG(1001, "web模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),

    /** iw-auth 授权模块 **/
    AUTH_TAG(2001, "授权模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),
    AUTH_APPLICATION_ACCOUNT_TYPE(2010, "应用账号-应用分类", DataType.CODE, RoleTypeEnum.USER),

    /** iw-eat 餐饮模块 **/
    EAT_TAG(3001, "餐饮模块", DataType.ID, RoleTypeEnum.SUPER_ADMIN),
    EAT_MEAL_TIME(3002, "餐饮-用餐时间", DataType.CODE, RoleTypeEnum.ADMIN), // MealTimeEnum
    EAT_DISHES_TYPE(3003, "餐饮-菜品分类", DataType.CODE, RoleTypeEnum.ADMIN), // DishesTypeEnum
    EAT_DISHES_STATUS(3004, "餐饮-菜品状态", DataType.CODE, RoleTypeEnum.ADMIN), // DishesStatusEnum
    EAT_FRIDGE_CATEGORY(3010, "冰箱-食材分类", DataType.CODE, RoleTypeEnum.USER),
    EAT_FRIDGE_SECTION(3011, "冰箱-食材分区", DataType.CODE, RoleTypeEnum.USER),

    /** iw-bookkeeping 记账模块 **/
    BOOKKEEPING_RECORD_TAG_CONSUME(4001, "记账-记录标签-支出", DataType.ID, RoleTypeEnum.USER),
    BOOKKEEPING_RECORD_TAG_INCOME(4011, "记账-记录标签-收入", DataType.ID, RoleTypeEnum.USER),
    BOOKKEEPING_RECORD_TYPE(4002, "记账-记录分类", DataType.CODE, RoleTypeEnum.USER),
    BOOKKEEPING_RECORD_CATEGORY(4003, "记账-记录类型", DataType.CODE, RoleTypeEnum.ADMIN), // RecordCategoryEnum
    BOOKKEEPING_MEMBERSHIP_TYPE(4004, "记账-会员类型", DataType.CODE, RoleTypeEnum.USER),
    BOOKKEEPING_MEMBERSHIP_BILLING_CYCLE(4005, "记账-会员计费周期", DataType.CODE, RoleTypeEnum.ADMIN), // MembershipBillingCycleEnum
    BOOKKEEPING_MEMBERSHIP_CYCLE_UNIT(4006, "记账-会员计费周期单位", DataType.CODE, RoleTypeEnum.ADMIN), // MembershipCycleUnitEnum
    ;

    private final Integer code;

    private final String name;

    private final DataType dataType;

    /**
     * 字典所属角色类型
     * <ul>
     *    <li>USER：普通用户字典，属于用户个人管理的字典。</li>
     *    <li>
     *        ADMIN：管理员字典，管理员及以上权限的角色可以管理，变更后会同步所有用户。<br/>
     *        一般会对应有一个服务端枚举值，在版本迭代后，管理员通过管理后台同步给所有用户使用。
     *    </li>
     *    <li>SUPER_ADMIN：超级管理员字典，属于应用级字典，不可维护。</li>
     * </ul>
     */
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

    public static DictTypeEnum getDictByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DictTypeEnum dictTypeEnum : DictTypeEnum.values()) {
            if (code.equals(dictTypeEnum.getCode())) {
                return dictTypeEnum;
            }
        }
        return null;
    }
}
