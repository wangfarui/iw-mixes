package com.itwray.iw.common.utils;

/**
 * {@link Number}工具类
 *
 * @author wray
 * @since 2024/4/26
 */
public abstract class NumberUtils {

    public static boolean isNotZero(Number number) {
        if (number == null) {
            return false;
        }
        return number.intValue() != 0;
    }

    public static boolean isNullOrZero(Number number) {
        if (number == null) {
            return true;
        }
        return number.intValue() == 0;
    }
}
