package com.itwray.iw.eat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.eat.model.entity.BaseFileRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传记录表 Mapper 接口
 *
 * @author wray
 * @since 2024/5/17
 */
@Mapper
public interface BaseFileRecordMapper extends BaseMapper<BaseFileRecordEntity> {
}
