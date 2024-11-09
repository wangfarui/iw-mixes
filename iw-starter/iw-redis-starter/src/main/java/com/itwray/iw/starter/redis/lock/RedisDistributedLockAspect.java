package com.itwray.iw.starter.redis.lock;

import com.itwray.iw.common.IwException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁拦截器
 *
 * @author wray
 * @since 2024/11/9
 */
@Aspect
@ConditionalOnClass(Aspect.class)
@Configuration
public class RedisDistributedLockAspect {

    private final RedissonClient redissonClient;

    @Autowired
    public RedisDistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        RedisLockUtil.setRedissonClient(redissonClient);
    }

    /**
     * SpEL 表达式解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        // 获取注解中的参数
        String lockName = parseLockName(distributedLock.lockName(), joinPoint);
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();
        TimeUnit timeUnit = distributedLock.timeUnit();

        // 使用Redisson获取锁
        RLock lock = redissonClient.getLock(lockName);
        boolean locked = false;

        try {
            // 尝试获取锁
            locked = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (locked) {
                // 成功获取锁，执行目标方法
                return joinPoint.proceed();
            } else {
                throw new IwException("Failed to acquire lock on " + lockName);
            }
        } finally {
            if (locked) {
                // 释放锁
                lock.unlock();
            }
        }
    }

    private String parseLockName(String lockName, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        StandardEvaluationContext context = new MethodBasedEvaluationContext(
                joinPoint.getTarget(),
                signature.getMethod(),
                joinPoint.getArgs(),
                new DefaultParameterNameDiscoverer()
        );

        Expression expression = parser.parseExpression(lockName);
        return RedisLockUtil.DISTRIBUTED_LOCK_NAME_PREFIX + expression.getValue(context, String.class);
    }
}
