package com.itwray.iw.web.model.enums;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.itwray.iw.web.model.entity.IdEntity;

import java.io.Serializable;

/**
 * 排序类型枚举接口
 *
 * @author wray
 * @since 2025/11/12
 */
public interface SortTypeEnum<T extends IdEntity<? extends Serializable>> extends BusinessConstantEnum {

    SFunction<T, ?> getSortField();

    static <T extends IdEntity<? extends Serializable>> SFunction<T, ?> getDefaultSortField(SortTypeEnum<T> sortTypeEnum) {
        if (sortTypeEnum == null) {
            return T::getId;
        }
        return sortTypeEnum.getSortField();
    }
}
