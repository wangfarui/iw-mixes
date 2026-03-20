package com.itwray.iw.web.support;

import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.model.vo.UserOwnerAware;
import com.itwray.iw.web.service.UserNameQueryService;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户归属字段填充支持类
 *
 * @author wray
 * @since 2026/3/19
 */
@Slf4j
public abstract class UserOwnerFillSupport {

    private static final Object USER_NAME_QUERY_SERVICE_LOCK = new Object();

    private static volatile UserNameQueryService userNameQueryService;

    private static volatile boolean userNameQueryServiceResolved = false;

    public static <T extends UserOwnerAware> T fill(T ownerVo) {
        if (ownerVo == null) {
            return null;
        }
        fill(Collections.singleton(ownerVo));
        return ownerVo;
    }

    public static <T extends UserOwnerAware> PageVo<T> fill(PageVo<T> pageVo) {
        if (pageVo == null) {
            return null;
        }
        fill(pageVo.getRecords());
        return pageVo;
    }

    public static void fill(Collection<? extends UserOwnerAware> ownerVoCollection) {
        if (ownerVoCollection == null || ownerVoCollection.isEmpty()) {
            return;
        }
        Integer currentUserId = UserUtils.getUserId(false);
        Map<Integer, String> userNameMap = queryUserNameMap(ownerVoCollection.stream()
                .map(UserOwnerAware::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        ownerVoCollection.forEach(ownerVo -> {
            ownerVo.setCanEdit(Objects.equals(ownerVo.getUserId(), currentUserId));
            String userName = userNameMap.get(ownerVo.getUserId());
            if (userName != null) {
                ownerVo.setUserName(userName);
            }
        });
    }

    private static Map<Integer, String> queryUserNameMap(Collection<Integer> userIdCollection) {
        if (userIdCollection == null || userIdCollection.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Integer> userIdList = userIdCollection.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (userIdList.isEmpty()) {
            return Collections.emptyMap();
        }
        UserNameQueryService userNameQueryService = getUserNameQueryService();
        if (userNameQueryService == null) {
            return Collections.emptyMap();
        }
        try {
            return Optional.ofNullable(userNameQueryService.queryUserNameMap(userIdList))
                    .orElse(Collections.emptyMap());
        } catch (Exception e) {
            log.error("批量查询用户名失败, userIds: {}", userIdList, e);
            return Collections.emptyMap();
        }
    }

    private static UserNameQueryService getUserNameQueryService() {
        if (userNameQueryServiceResolved) {
            return userNameQueryService;
        }
        synchronized (USER_NAME_QUERY_SERVICE_LOCK) {
            if (userNameQueryServiceResolved) {
                return userNameQueryService;
            }
            if (!ApplicationContextHolder.hasApplicationContext()) {
                return null;
            }
            userNameQueryService = ApplicationContextHolder.getBeanProvider(UserNameQueryService.class)
                    .orderedStream()
                    .findFirst()
                    .orElse(null);
            userNameQueryServiceResolved = true;
            return userNameQueryService;
        }
    }
}
