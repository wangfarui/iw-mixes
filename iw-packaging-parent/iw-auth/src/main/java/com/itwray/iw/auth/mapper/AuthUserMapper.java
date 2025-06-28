package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author wray
 * @since 2024/3/2
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUserEntity> {

    @IgnorePermission
    List<AuthUserEntity> queryAllUser();
}
