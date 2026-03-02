package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.dao.BaseWebsiteNavigationDao;
import com.itwray.iw.auth.mapper.BaseWebsiteNavigationMapper;
import com.itwray.iw.auth.model.dto.WebsiteNavigationAddDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationPageDto;
import com.itwray.iw.auth.model.dto.WebsiteNavigationUpdateDto;
import com.itwray.iw.auth.model.entity.BaseWebsiteNavigationEntity;
import com.itwray.iw.auth.model.vo.WebsiteNavigationDetailVo;
import com.itwray.iw.auth.model.vo.WebsiteNavigationListVo;
import com.itwray.iw.auth.model.vo.WebsiteNavigationPageVo;
import com.itwray.iw.auth.service.BaseWebsiteNavigationService;
import com.itwray.iw.common.constants.CommonConstants;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 网站导航记录 服务实现类
 *
 * @author wray
 * @since 2026-02-28
 */
@Service
public class BaseWebsiteNavigationServiceImpl extends WebServiceImpl<BaseWebsiteNavigationDao, BaseWebsiteNavigationMapper, BaseWebsiteNavigationEntity,
        WebsiteNavigationAddDto, WebsiteNavigationUpdateDto, WebsiteNavigationDetailVo, Integer> implements BaseWebsiteNavigationService {

    @Autowired
    public BaseWebsiteNavigationServiceImpl(BaseWebsiteNavigationDao baseDao) {
        super(baseDao);
    }

    @Override
    @Transactional
    public Integer add(WebsiteNavigationAddDto dto) {
        BaseWebsiteNavigationEntity entity = BeanUtil.copyProperties(dto, BaseWebsiteNavigationEntity.class);
        entity.setTags(this.serializeTags(dto.getTags()));
        getBaseDao().save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void update(WebsiteNavigationUpdateDto dto) {
        getBaseDao().queryById(dto.getId());
        BaseWebsiteNavigationEntity entity = BeanUtil.copyProperties(dto, BaseWebsiteNavigationEntity.class);
        entity.setTags(this.serializeTags(dto.getTags()));
        getBaseDao().updateById(entity);
    }

    @Override
    public WebsiteNavigationDetailVo detail(Integer id) {
        BaseWebsiteNavigationEntity entity = getBaseDao().queryById(id);
        WebsiteNavigationDetailVo detailVo = BeanUtil.copyProperties(entity, WebsiteNavigationDetailVo.class);
        detailVo.setTags(this.deserializeTags(entity.getTags()));
        return detailVo;
    }

    @Override
    public PageVo<WebsiteNavigationPageVo> page(WebsiteNavigationPageDto dto) {
        LambdaQueryWrapper<BaseWebsiteNavigationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), BaseWebsiteNavigationEntity::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getCategory()), BaseWebsiteNavigationEntity::getCategory, dto.getCategory())
                .eq(dto.getStatus() != null, BaseWebsiteNavigationEntity::getStatus, dto.getStatus())
                .eq(dto.getShared() != null, BaseWebsiteNavigationEntity::getShared, dto.getShared())
                .like(StringUtils.isNotBlank(dto.getTag()), BaseWebsiteNavigationEntity::getTags, dto.getTag());
        queryWrapper.orderByDesc(BaseWebsiteNavigationEntity::getId);
        return getBaseDao().page(dto, queryWrapper, entity -> {
            WebsiteNavigationPageVo pageVo = BeanUtil.copyProperties(entity, WebsiteNavigationPageVo.class);
            pageVo.setTags(this.deserializeTags(entity.getTags()));
            return pageVo;
        });
    }

    @Override
    public List<WebsiteNavigationListVo> querySharedWebsiteList() {
        List<BaseWebsiteNavigationEntity> list = getBaseDao().querySharedWebsiteList();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(entity -> {
            WebsiteNavigationListVo vo = BeanUtil.copyProperties(entity, WebsiteNavigationListVo.class);
            vo.setTags(this.deserializeTags(entity.getTags()));
            return vo;
        }).toList();
    }

    private String serializeTags(List<String> tags) {
        if (tags == null) {
            return null;
        }
        if (CollUtil.isEmpty(tags)) {
            return CommonConstants.EMPTY;
        }
        List<String> tagList = tags.stream().filter(StringUtils::isNotBlank).toList();
        if (CollUtil.isEmpty(tagList)) {
            return CommonConstants.EMPTY;
        }
        return JSONUtil.toJsonStr(tagList);
    }

    private List<String> deserializeTags(String tags) {
        if (StringUtils.isBlank(tags)) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.parseArray(tags).toList(String.class);
        } catch (Exception e) {
            return Arrays.stream(tags.split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .toList();
        }
    }
}
