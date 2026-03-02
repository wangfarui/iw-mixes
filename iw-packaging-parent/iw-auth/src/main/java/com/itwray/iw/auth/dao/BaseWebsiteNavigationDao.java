package com.itwray.iw.auth.dao;

import com.itwray.iw.auth.mapper.BaseWebsiteNavigationMapper;
import com.itwray.iw.auth.model.entity.BaseWebsiteNavigationEntity;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网站导航记录表 DAO
 *
 * @author wray
 * @since 2026-02-28
 */
@Component
public class BaseWebsiteNavigationDao extends BaseDao<BaseWebsiteNavigationMapper, BaseWebsiteNavigationEntity> {

    public List<BaseWebsiteNavigationEntity> querySharedWebsiteList() {
        return getBaseMapper().querySharedWebsiteList();
    }
}
