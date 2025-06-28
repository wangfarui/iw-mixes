package com.itwray.iw.starter.redis.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author wray
 * @since 2024/11/9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的名称，可以用 SpEL 表达式支持动态获取方法参数
     * <p>支持表达式如下：
     * <ul>
     *     <li>固定字符串: 'id'</li>
     *     <li>方法的参数: #orderId</li>
     *     <li>方法参数对象中的指定参数: #order.id</li>
     *     <li>方法参数中的Map: #map['id']</li>
     *     <li>方法参数中的List: #list[index]</li>
     *     <li>方法参数中对象套对象: #order.subOrder.id</li>
     * </ul>
     * <p>注意! 在 SpEL 表达式中, 单引号是 SpEL 解析字符串常量的要求!
     */
    String lockName();

    long waitTime() default 10L; // 等待获取锁的最大时间，默认10秒

    long leaseTime() default 30L; // 锁持有时间，默认30秒

    TimeUnit timeUnit() default TimeUnit.SECONDS; // 时间单位，默认秒
}
