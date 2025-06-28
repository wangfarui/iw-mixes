package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

import java.util.Set;

/**
 * 记账记录-记录分类的默认枚举值
 *
 * @author farui.wang
 * @since 2025/6/11
 */
@Getter
public enum BookkeepingRecordTypeDefaultEnum implements ConstantEnum {

    EAT(1, "餐饮美食", new String[]{"餐饮", "零食"}),
    STORE(2, "日用百货", new String[]{"穿搭美容", "购物", "生活日用", "运动", "养娃", "医疗保健", "宠物"}),
    TRAFFIC(3, "交通出行", new String[]{"交通", "酒店旅行", "爱车"}),
    PAY(4, "充值缴费", new String[]{"通讯", "休闲玩乐"}),
    LIFE(5, "生活服务", new String[]{"工资", "人情社交", "生活服务", "学习", "住房"}),
    OTHER(6, "其他", new String[]{"公益", "生意", "转账", "中奖", "金融保险", "保险理赔"}),
    IGNORE(0, "忽略分类", new String[]{"投资理财", "退款", "借还款"})
    ;

    private final Integer code;

    private final String name;

    private final Set<String> keys;

    BookkeepingRecordTypeDefaultEnum(Integer code, String name, String[] keys) {
        this.code = code;
        this.name = name;
        this.keys = Set.of(keys);
    }

    public static BookkeepingRecordTypeDefaultEnum confirmRecordType(String typeKey) {
        for (BookkeepingRecordTypeDefaultEnum recordTypeDefaultEnum : BookkeepingRecordTypeDefaultEnum.values()) {
            if (recordTypeDefaultEnum.keys.contains(typeKey)) {
                return recordTypeDefaultEnum;
            }
        }
        // 无法确定的情况下,默认返回其他
        return BookkeepingRecordTypeDefaultEnum.OTHER;
    }
}
