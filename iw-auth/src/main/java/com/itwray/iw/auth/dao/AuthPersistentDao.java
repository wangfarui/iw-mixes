package com.itwray.iw.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.auth.mapper.AuthPersistentMapper;
import com.itwray.iw.auth.model.entity.AuthPersistent;
import org.springframework.stereotype.Component;

/**
 * 认证信息持久化 Dao
 *
 * @author wray
 * @since 2024/3/20
 */
@Component
public class AuthPersistentDao extends ServiceImpl<AuthPersistentMapper, AuthPersistent> {
}
