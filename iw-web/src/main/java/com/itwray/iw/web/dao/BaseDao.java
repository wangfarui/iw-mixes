package com.itwray.iw.web.dao;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.dto.PageDto;
import com.itwray.iw.web.model.entity.IdEntity;
import com.itwray.iw.web.model.vo.PageVo;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基础DAO
 *
 * @author wray
 * @since 2024/4/25
 */
public class BaseDao<M extends BaseMapper<T>, T extends IdEntity> extends ServiceImpl<M, T> {

    /**
     * 根据id查询实体对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    public T queryById(Serializable id) {
        return queryById(id, "数据不存在，请刷新重试！");
    }

    /**
     * 根据id查询实体对象
     *
     * @param id     主键id
     * @param errMsg 查询为空后的异常提醒信息
     * @return 实体对象
     */
    public T queryById(Serializable id, String errMsg) {
        T entity = this.getById(id);
        // 物理数据不存在或已逻辑删除
        if (entity == null) {
            throw new BusinessException(errMsg);
        }
        return entity;
    }

    /**
     * 分页查询实体对象
     *
     * @param pageDto      PageDto分页请求对象
     * @param queryWrapper 实体查询条件
     * @param <P>          PageDto
     * @return PageVo分页响应对象
     */
    public <P extends PageDto> PageVo<T> page(P pageDto, Wrapper<T> queryWrapper) {
        return this.page(pageDto, queryWrapper, getEntityClass());
    }

    public <P extends PageDto, R> PageVo<R> page(P pageDto, Wrapper<T> queryWrapper, Class<R> resultType) {
        return this.page(pageDto, queryWrapper, record -> BeanUtil.copyProperties(record, resultType));
    }

    public <P extends PageDto, R> PageVo<R> page(P pageDto, Wrapper<T> queryWrapper, Function<? super T, ? extends R> mapper) {
        PageVo<T> pageVo = new PageVo<>(pageDto);
        this.page(pageVo, queryWrapper);
        List<R> resultList = pageVo.getRecords().stream().map(mapper).collect(Collectors.toList());
        return PageVo.of(pageVo, resultList);
    }

}
