package com.itwray.iw.web.model.enums;

import com.itwray.iw.common.ConstantEnum;
import com.itwray.iw.web.exception.IwWebException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 订单编号 枚举
 *
 * @author wray
 * @since 2025/3/14
 */
@Getter
public enum OrderNoEnum implements ConstantEnum {

    BOOKKEEPING_RECORDS(1001, "记账记录");

    private final Integer code;

    private final String name;

    OrderNoEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    static {
        Set<Integer> set = new HashSet<>();
        // 自校验订单编号枚举类的code是否合规
        for (OrderNoEnum orderNoEnum : values()) {
            if (orderNoEnum.getCode() > 9999 || orderNoEnum.getCode() < 1000) {
                throw new IwWebException("OrderNoEnum 枚举code值是非法的!!!");
            }
            if (!set.add(orderNoEnum.getCode())) {
                throw new IwWebException("OrderNoEnum 枚举code定义重复, 请确保code唯一!!!");
            }
        }
    }
}
