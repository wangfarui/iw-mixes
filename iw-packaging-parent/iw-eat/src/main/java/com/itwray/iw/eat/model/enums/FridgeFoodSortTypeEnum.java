package com.itwray.iw.eat.model.enums;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.itwray.iw.eat.model.entity.EatFridgeFoodEntity;
import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import com.itwray.iw.web.model.enums.SortTypeEnum;
import lombok.Getter;

/**
 * 冰箱食材排序类型枚举
 *
 * @author wray
 * @since 2026/1/21
 */
@Getter
public enum FridgeFoodSortTypeEnum implements BusinessConstantEnum, SortTypeEnum<EatFridgeFoodEntity> {

    DEFAULT(0, "id", "默认"),
    ADD_DATE(1, "add_date", "入库日期"),
    EXPIRE_DATE(2, "expire_date", "过期日期");

    private final Integer code;

    private final String name;

    private final String desc;

    FridgeFoodSortTypeEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public SFunction<EatFridgeFoodEntity, ?> getSortField() {
        if (this == DEFAULT) {
            return EatFridgeFoodEntity::getId;
        } else if (this == ADD_DATE) {
            return EatFridgeFoodEntity::getAddDate;
        } else if (this == EXPIRE_DATE) {
            return EatFridgeFoodEntity::getExpireDate;
        }
        return EatFridgeFoodEntity::getId;
    }
}
