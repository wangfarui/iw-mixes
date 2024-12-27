package com.itwray.iw.auth.job;

import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.common.constants.EnableEnum;
import com.itwray.iw.starter.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户定时任务
 *
 * @author wray
 * @since 2024/12/27
 */
@Component
@Slf4j
public class AuthUserScheduledJob {

    private final AuthUserDao authUserDao;

    public AuthUserScheduledJob(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    /**
     * 每隔10分钟定时清除用户历史过期token
     * TODO 前提条件：auth服务是单实例、用户数据量较小
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    public void clearUserToken() {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("[clearUserToken]任务开始执行");
        List<Integer> userEntityList = authUserDao.lambdaQuery()
                .eq(AuthUserEntity::getEnabled, EnableEnum.ENABLE.getCode())
                .select(AuthUserEntity::getId)
                .list()
                .stream()
                .map(AuthUserEntity::getId)
                .toList();

        for (Integer userId : userEntityList) {
            Set<String> userTokens = RedisUtil.members(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(userId), String.class);
            if (userTokens != null) {
                int count = 0;
                for (String token : userTokens) {
                    // 如果token已过期，则删除set集合中的value
                    if (!RedisUtil.hasKey(AuthRedisKeyEnum.USER_TOKEN_KEY.getKey(token))) {
                        RedisUtil.remove(AuthRedisKeyEnum.USER_TOKEN_SET_KEY.getKey(userId), token);
                        count++;
                    }
                }
                if (count > 0) {
                    log.info("用户[{}]成功清理{}个过期token", userId, count);
                }
            }
        }
        log.info("[clearUserToken]任务执行完毕, 执行用时: {}s", (System.currentTimeMillis() - currentTimeMillis) / 1000);
    }
}
