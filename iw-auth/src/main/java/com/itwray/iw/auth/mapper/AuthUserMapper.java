package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.AuthRole;
import com.itwray.iw.auth.model.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author wray
 * @since 2024/3/2
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {

    List<AuthRole> queryRolesByUserId(Long userId);
}
