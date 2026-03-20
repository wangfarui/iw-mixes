package com.itwray.iw.auth.config;

import com.itwray.iw.auth.client.AuthUserClient;
import com.itwray.iw.auth.model.vo.UserSimpleVo;
import com.itwray.iw.web.service.UserNameQueryService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Auth 用户客户端自动装配
 *
 * @author wray
 * @since 2026/3/19
 */
@AutoConfiguration
public class AuthUserClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(UserNameQueryService.class)
    public UserNameQueryService userNameQueryService(AuthUserClient authUserClient) {
        return userIdCollection -> this.queryUserNameMap(authUserClient, userIdCollection);
    }

    private Map<Integer, String> queryUserNameMap(AuthUserClient authUserClient, Collection<Integer> userIdCollection) {
        if (userIdCollection == null || userIdCollection.isEmpty()) {
            return Collections.emptyMap();
        }
        return authUserClient.querySimpleUserList(userIdCollection.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserSimpleVo::getId, UserSimpleVo::getName, (left, right) -> left));
    }
}
