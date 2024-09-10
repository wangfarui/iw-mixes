package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.vo.BaseDictVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;

import java.util.List;

/**
 * 字典接口服务
 *
 * @author wray
 * @since 2024/5/26
 */
public interface BaseDictService {

    /**
     * 获取字典类型集合
     *
     * @return 字典类型集合
     */
    List<DictTypeVo> getDictTypeList();

    /**
     * 根据字典类型获取字典信息集合
     *
     * @param dictType 字典类型code
     * @return 字典信息集合
     */
    List<BaseDictVo> getDictList(Integer dictType);
}
