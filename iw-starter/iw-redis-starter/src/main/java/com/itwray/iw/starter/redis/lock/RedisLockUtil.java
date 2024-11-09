package com.itwray.iw.starter.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, RLock> LOCK_MAP = new HashMap<>();

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
        RLock lock = redissonClient.getLock(fullLockName(lockName));
        lock.lock();
        LOCK_MAP.put(lockName, lock);
    }

    /**
     * 释放分布式锁
     *
     * @param lockName 锁名称
     */
    public static void unlock(String lockName) {
        RLock lock = LOCK_MAP.get(lockName);
        if (lock != null) {
            lock.unlock();
        }
    }

    private static String fullLockName(String lockName) {
        return DISTRIBUTED_LOCK_NAME_PREFIX + lockName;
    }
}
