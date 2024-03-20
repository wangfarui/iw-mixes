package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.AuthPersistent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 授权信息持久化 Mapper
 *
 * @author wray
 * @since 2024/3/20
 */
@Mapper
public interface AuthPersistentMapper extends BaseMapper<AuthPersistent> {
}
