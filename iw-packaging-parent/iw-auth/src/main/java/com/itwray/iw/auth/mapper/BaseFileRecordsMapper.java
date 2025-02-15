package com.itwray.iw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.auth.model.entity.BaseFileRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传记录表 Mapper 接口
 *
 * @author wray
 * @since 2024/5/17
 */
@Mapper
public interface BaseFileRecordsMapper extends BaseMapper<BaseFileRecordsEntity> {
}
