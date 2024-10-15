package com.itwray.iw.web.constants;

/**
 * web公共常量
 *
 * @author wray
 * @since 2024/5/15
 */
public abstract class WebCommonConstants {

    /**
     * 数据库 limit 1 固定语法
     */
    public static final String LIMIT_ONE = "limit 1";

    /**
     * 金额的余数
     */
    public static final Integer AMOUNT_SCALE = 2;

    /**
     * 返回标准的limit语法
     *
     * @param limitNum limit数量
     * @return limit #{limitNum}
     */
    public static String standardLimit(Integer limitNum) {
        if (limitNum == null || limitNum <= 0) {
            throw new IllegalArgumentException("limitNum is illegal");
        }
        return "limit " + limitNum;
    }
}
