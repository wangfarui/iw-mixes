package com.itwray.iw.common.utils;

import com.itwray.iw.common.ConstantEnum;

/**
 * 基于 {@link ConstantEnum} 工具类
 *
 * @author wray
 * @since 2025/3/25
 */
public abstract class ConstantEnumUtil {

    public static <T extends ConstantEnum> T findByType(Class<T> constantEnumClass, Integer code) {
        if (code == null || constantEnumClass == null) {
            return null;
        }
        T[] enumConstants = constantEnumClass.getEnumConstants();
        if (enumConstants == null) {
            return null;
        }
        for (T element : enumConstants) {
            if (element.getCode().equals(code)) {
                return element;
            }
        }
        return null;
    }

    public static <T extends ConstantEnum> String getNameByCode(Class<T> constantEnumClass, Integer code) {
        T constantEnum = findByType(constantEnumClass, code);
        return constantEnum != null ? constantEnum.getName() : null;
    }

    public static <T extends ConstantEnum> boolean isEnumCode(Class<T> constantEnumClass, Integer code) {
        return findByType(constantEnumClass, code) != null;
    }
}
