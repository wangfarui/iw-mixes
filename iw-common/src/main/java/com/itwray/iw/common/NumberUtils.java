package com.itwray.iw.common;

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
}
