package com.itwray.iw.starter.redis;

/**
 * Redis Key 管理器
 *
 * @author wray
 * @since 2024/12/22
 */
public interface RedisKeyManager {

    /**
     * Key的前缀格式
     */
    String getPattern();

    /**
     * Key的过期时间
     * <p>值为null或<=0时，表示不过期。但是不建议Key有效期设置为不过期!!!</p>
     *
     * @return 秒
     */
    Long getExpireTime();

    /**
     * 获取Redis Key
     *
     * @param args Redis Key的格式化参数
     * @return 完整的Redis Key
     */
    default String getKey(Object... args) {
        return String.format(getPattern(), args);
    }

    /**
     * 获取Redis Key为String类型的Value
     *
     * @param valueType Value的类型
     * @param args      key的参数
     * @param <T>       valueType
     * @return valueType类型的Value值
     */
    default <T> T getStringValue(Class<T> valueType, Object... args) {
        return RedisUtil.get(getKey(args), valueType);
    }

    /**
     * 删除Redis Key为String类型的Value
     *
     * @param args key的参数
     */
    default void delete(Object... args) {
        RedisUtil.delete(getKey(args));
    }

    /**
     * 使用当前Redis Key枚举, 写入Key为String类型的Redis值
     *
     * @param value Redis Key的值
     * @param args  Redis Key的格式化参数
     */
    default void setStringValue(Object value, Object... args) {
        RedisUtil.set(getKey(args), value, getExpireTime());
    }

    /**
     * 使用当前Redis Key枚举, 设置Redis Key的有效期
     *
     * @param args Redis Key的格式化参数
     */
    default void setExpire(Object... args) {
        RedisUtil.expire(getKey(args), getExpireTime());
    }
}
