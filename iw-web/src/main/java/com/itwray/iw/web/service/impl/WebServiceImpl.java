package com.itwray.iw.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.dto.UpdateDto;
import com.itwray.iw.web.model.entity.IdEntity;
import com.itwray.iw.web.model.vo.DetailVo;
import com.itwray.iw.web.service.WebService;
import lombok.Getter;

import java.io.Serializable;

/**
 * wen抽象服务实现层
 *
 * @author wray
 * @since 2024/9/11
 */
public abstract class WebServiceImpl<M extends BaseMapper<T>, T extends IdEntity, D extends BaseDao<M, T>, L extends DetailVo> implements WebService {

    private final D baseDao;

    protected final Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(getClass(), WebServiceImpl.class);

    @Getter
    protected final Class<L> detailClass = currentDetailClass();

    public WebServiceImpl(D baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Serializable add(AddDto dto) {
        T entity = BeanUtil.copyProperties(dto, getBaseDao().getEntityClass());
        getBaseDao().save(entity);
        return entity.getId();
    }

    @Override
    public void update(UpdateDto dto) {
        getBaseDao().queryById(dto.getId());
        T entity = BeanUtil.copyProperties(dto, getBaseDao().getEntityClass());
        getBaseDao().updateById(entity);
    }

    @Override
    public void delete(Serializable id) {
        getBaseDao().removeById(id);
    }

    @Override
    public L detail(Serializable id) {
        T entity = getBaseDao().queryById(id);
        return BeanUtil.copyProperties(entity, getDetailClass());
    }

    @SuppressWarnings("unchecked")
    protected Class<L> currentDetailClass() {
        return (Class<L>) this.typeArguments[3];
    }

    protected D getBaseDao() {
        return this.baseDao;
    }
}
