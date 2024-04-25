package com.itwray.iw.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.web.IwWebException;

import java.io.Serializable;

/**
 * 基础DAO
 *
 * @author wray
 * @since 2024/4/25
 */
public class BaseDao<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public T queryById(Serializable id) {
        return queryById(id, "数据不存在，请刷新重试！");
    }

    public T queryById(Serializable id, String errMsg) {
        T entity = this.getById(id);
        if (entity == null) {
            throw new IwWebException(errMsg);
        }
        return entity;
    }
}
