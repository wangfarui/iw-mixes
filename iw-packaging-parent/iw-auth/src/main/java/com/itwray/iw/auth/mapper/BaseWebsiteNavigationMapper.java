package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.BaseWebsiteNavigationEntity;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 网站导航记录表 Mapper 接口
 *
 * @author wray
 * @since 2026-02-28
 */
@Mapper
public interface BaseWebsiteNavigationMapper extends BaseMapper<BaseWebsiteNavigationEntity> {

    @IgnorePermission
    List<BaseWebsiteNavigationEntity> querySharedWebsiteList();
}
