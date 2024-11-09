package com.itwray.iw.starter.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具
 *
 * @author wray
 * @since 2024/11/9
 */
public abstract class RedisLockUtil {

    /**
     * 分布式锁名称前缀
     */
    public static final String DISTRIBUTED_LOCK_NAME_PREFIX = "DLock:";

    private static RedissonClient redissonClient;

    static void setRedissonClient(RedissonClient redissonClient) {
        RedisLockUtil.redissonClient = redissonClient;
    }

    /**
     * 获取分布式锁
     * <p>一直尝试获取锁，直到获取锁成功
     * <p>不会自动释放锁(会被看门狗进行锁续期操作), 需要主动调用{@link RedisLockUtil#unlock(String)}方法释放锁
     *
     * @param lockName 锁名称
     */
    public static void lock(String lockName) {
        RLock lock = getRLockByName(lockName);
        lock.lock();
    }

    /**
     * 获取分布式锁
     * <p>在 waitTime(s) 时间范围内尝试获取锁
     * <p>获取到锁后，持有 leaseTime(s) 后释放（不会进行锁续期）
     *
     * @param lockName  锁名称
     * @param waitTime  获取分布式锁的等待时间
     * @param leaseTime 分布式锁的持有时间
     * @return 获取分布式锁的结果 true -> 成功获取
     */
    public static boolean tryLock(String lockName, long waitTime, long leaseTime) {
        RLock lock = getRLockByName(lockName);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockName 锁名称
     */
    public static void unlock(String lockName) {
        RLock lock = getRLockByName(lockName);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    private static RLock getRLockByName(String lockName) {
        return redissonClient.getLock(DISTRIBUTED_LOCK_NAME_PREFIX + lockName);
    }
}
