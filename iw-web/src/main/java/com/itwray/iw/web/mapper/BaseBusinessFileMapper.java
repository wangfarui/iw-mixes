package com.itwray.iw.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.web.model.entity.BaseBusinessFileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务文件关联表 Mapper 接口
 *
 * @author wray
 * @since 2025-04-23
 */
@Mapper
public interface BaseBusinessFileMapper extends BaseMapper<BaseBusinessFileEntity> {

}
