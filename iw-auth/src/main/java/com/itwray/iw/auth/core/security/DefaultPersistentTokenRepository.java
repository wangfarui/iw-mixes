package com.itwray.iw.auth.core.security;

import com.itwray.iw.auth.dao.AuthPersistentDao;
import com.itwray.iw.auth.model.entity.AuthPersistent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 默认持久化令牌
 *
 * @author wray
 * @since 2024/3/20
 */
@Slf4j
public class DefaultPersistentTokenRepository implements PersistentTokenRepository {

    private final AuthPersistentDao authPersistentDao;

    public DefaultPersistentTokenRepository(AuthPersistentDao authPersistentDao) {
        this.authPersistentDao = authPersistentDao;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        AuthPersistent authPersistent = new AuthPersistent();
        authPersistent.setUsername(token.getUsername());
        authPersistent.setSeries(token.getSeries());
        authPersistent.setToken(token.getTokenValue());
        authPersistent.setLastUsed(this.transformDate(token.getDate()));
        authPersistentDao.save(authPersistent);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        boolean res = authPersistentDao.lambdaUpdate()
                .eq(AuthPersistent::getSeries, series)
                .set(AuthPersistent::getToken, tokenValue)
                .set(AuthPersistent::getLastUsed, this.transformDate(lastUsed))
                .update();
        if (!res) {
            log.info("用户授权持久化信息更新失败, series: {}", series);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        AuthPersistent authPersistent = authPersistentDao.getById(seriesId);
        if (authPersistent == null) {
            log.debug("用户授权持久化信息不存在, seriesId: {}", seriesId);
            return null;
        }
        return new PersistentRememberMeToken(authPersistent.getUsername(), authPersistent.getSeries(),
                authPersistent.getToken(), this.transformLocalDateTime(authPersistent.getLastUsed()));
    }

    @Override
    public void removeUserTokens(String username) {
        boolean res = authPersistentDao.lambdaUpdate()
                .eq(AuthPersistent::getUsername, username)
                .remove();
        if (!res) {
            log.debug("用户授权持久化信息移除失败, username: {}", username);
        }
    }

    private LocalDateTime transformDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 不考虑时区的时间对象转换
     */
    private Date transformLocalDateTime(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return new Date(timestamp.getTime());
    }
}
