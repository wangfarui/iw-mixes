package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.vo.DictAllListVo;
import com.itwray.iw.auth.model.vo.DictListVo;
import com.itwray.iw.auth.model.vo.DictPageVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;
import java.util.Map;

/**
 * 字典接口服务
 *
 * @author wray
 * @since 2024/5/26
 */
public interface BaseDictService extends WebService {

    /**
     * 获取字典类型集合
     *
     * @return 字典类型集合
     */
    List<DictTypeVo> getDictTypeList();

    /**
     * 根据字典类型获取字典列表信息集合
     *
     * @param dictType 字典类型code
     * @return 字典列表信息集合
     */
    List<DictListVo> getDictList(Integer dictType);

    /**
     * 查询所有字典类型的字典列表
     *
     * @param latest 查询最新的数据
     * @return 字典列表精简信息集合
     */
    Map<String, List<DictAllListVo>> getAllDictList(Boolean latest);

    /**
     * 分页查询字典信息
     *
     * @param dto 分页查询对象
     * @return 字典分页对象
     */
    PageVo<DictPageVo> page(DictPageDto dto);
}