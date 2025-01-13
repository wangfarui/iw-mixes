package com.itwray.iw.web.service;

import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.dto.UpdateDto;
import com.itwray.iw.web.model.vo.DetailVo;

import java.io.Serializable;

/**
 * web服务Service接口规范
 *
 * @author wray
 * @since 2024/9/11
 */
public interface WebService<A extends AddDto, U extends UpdateDto, V extends DetailVo> {

    Serializable add(A dto);

    void update(U dto);

    void delete(Serializable id);

    V detail(Serializable id);
}
