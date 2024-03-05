package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {
}
