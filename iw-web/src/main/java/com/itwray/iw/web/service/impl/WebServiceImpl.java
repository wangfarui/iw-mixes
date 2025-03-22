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
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * wen抽象服务实现层
 *
 * @author wray
 * @since 2024/9/11
 */
public abstract class WebServiceImpl<D extends BaseDao<M, T>, M extends BaseMapper<T>, T extends IdEntity<ID>,
        A extends AddDto, U extends UpdateDto, V extends DetailVo, ID extends Serializable> implements WebService<A, U, V, ID> {

    private final D baseDao;

    protected final Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(getClass(), WebServiceImpl.class);

    @Getter
    protected final Class<V> detailClass = currentDetailClass();

    public WebServiceImpl(D baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    @Transactional
    public ID add(A dto) {
        T entity = BeanUtil.copyProperties(dto, getBaseDao().getEntityClass());
        getBaseDao().save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void update(U dto) {
        getBaseDao().queryById(dto.getId());
        T entity = BeanUtil.copyProperties(dto, getBaseDao().getEntityClass());
        getBaseDao().updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        getBaseDao().removeById(id);
    }

    @Override
    public V detail(ID id) {
        T entity = getBaseDao().queryById(id);
        return BeanUtil.copyProperties(entity, getDetailClass());
    }

    @SuppressWarnings("unchecked")
    protected Class<V> currentDetailClass() {
        return (Class<V>) this.typeArguments[5];
    }

    protected D getBaseDao() {
        return this.baseDao;
    }
}
