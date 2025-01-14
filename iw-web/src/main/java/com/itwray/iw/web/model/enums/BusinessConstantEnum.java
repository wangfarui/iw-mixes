package com.itwray.iw.web.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.itwray.iw.common.ConstantEnum;

/**
 * 业务常量枚举
 * <p>基于mybatis-plus实现实体枚举映射</p>
 *
 * @author wray
 * @since 2025/1/13
 */
public interface BusinessConstantEnum extends IEnum<Integer>, ConstantEnum {

    @Override
    @JsonValue
    default Integer getValue() {
        return getCode();
    }
}
