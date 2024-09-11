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
public interface WebService {

    Serializable add(AddDto dto);

    void update(UpdateDto dto);

    void delete(Serializable id);

    DetailVo detail(Serializable id);
}
