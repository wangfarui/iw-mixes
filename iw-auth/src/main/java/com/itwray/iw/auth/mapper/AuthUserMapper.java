package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author wray
 * @since 2024/3/2
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUserEntity> {

}
