package com.itwray.iw.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表 Mapper 接口
 *
 * @author wray
 * @since 2024-05-26
 */
@Mapper
public interface BaseDictMapper extends BaseMapper<BaseDictEntity> {

}
