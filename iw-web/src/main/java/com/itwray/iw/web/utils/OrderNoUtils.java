package com.itwray.iw.web.utils;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import com.itwray.iw.starter.redis.CommonRedisKeyEnum;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.model.enums.OrderNoEnum;

import java.time.LocalDate;

/**
 * 订单编号工具类
 * <p>
 * 编号固定格式为  <8位年月日> + <6位随机数> + <4位业务前缀> + <4位自增序号> + <8位随机数>
 * <p>
 * 依赖 Redis 缓存中间件!!!
 *
 * @author wray
 * @see OrderNoEnum
 * @since 2025/3/14
 */
public abstract class OrderNoUtils {

    /**
     * 获取当天的订单编号
     *
     * @param orderNoEnum 订单编号枚举
     * @return 订单编号
     */
    public static String getAndIncrement(OrderNoEnum orderNoEnum) {
        return getAndIncrement(orderNoEnum, LocalDate.now());
    }

    /**
     * 获取指定日期的订单编号
     *
     * @param orderNoEnum 订单编号枚举
     * @param currentDate 指定日期
     * @return 订单编号
     */
    public static String getAndIncrement(OrderNoEnum orderNoEnum, LocalDate currentDate) {
        String dateStr = currentDate.toString().replace("-", "").substring(2);
        Long no = RedisUtil.incrementOne(CommonRedisKeyEnum.ORDER_NO_KEY.getKey(orderNoEnum.getCode(), dateStr));
        CommonRedisKeyEnum.ORDER_NO_KEY.setExpire(orderNoEnum.getCode(), dateStr);
        Pair<String, String> pair = generateRandomNumbers();
        return dateStr + pair.getKey() + UserUtils.getUserId() + orderNoEnum.getCode() + pair.getValue() + String.format("%03d", no);
    }

    private static Pair<String, String> generateRandomNumbers() {
        String left = RandomUtil.randomNumbers(4);
        String right = RandomUtil.randomNumbers(4);
        return Pair.of(left, right);
    }
}
