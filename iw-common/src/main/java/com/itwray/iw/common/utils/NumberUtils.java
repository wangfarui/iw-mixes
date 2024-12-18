package com.itwray.iw.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * {@link Number}工具类
 *
 * @author wray
 * @since 2024/4/26
 */
public abstract class NumberUtils {

    /**
     * 电话号码正则表达式
     */
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 数值是否不为0
     *
     * @param number 数值
     * @return true -> 数值不为0
     */
    public static boolean isNotZero(Number number) {
        if (number == null) {
            return false;
        }
        return number.intValue() != 0;
    }

    /**
     * 数值是否为null或0
     *
     * @param number 数值
     * @return true -> 数值为null或0
     */
    public static boolean isNullOrZero(Number number) {
        if (number == null) {
            return true;
        }
        return number.intValue() == 0;
    }

    /**
     * 校验是否为合法的电话号码
     *
     * @param phoneNumber 电话号码
     * @return true -> 合法电话号码
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
}
