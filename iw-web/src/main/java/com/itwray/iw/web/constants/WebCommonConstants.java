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
     * 内部客户端ip
     */
    public static final String INNER_CLIENT_IP = "-1";

    /**
     * 数据库默认int值
     */
    public static final Integer DATABASE_DEFAULT_INT_VALUE = 0;

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

    /**
     * 返回标准的limit分页语法
     *
     * @param currentPage 当前页
     * @param pageSize    每页数量
     * @return limit #{startNum}, #{pageSize}
     */
    public static String standardPageLimit(Integer currentPage, Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("pageSize is illegal");
        }
        return "limit " + ((currentPage == null || currentPage <= 0) ? 0 : (currentPage - 1) * pageSize) + ", " + pageSize;
    }
}
