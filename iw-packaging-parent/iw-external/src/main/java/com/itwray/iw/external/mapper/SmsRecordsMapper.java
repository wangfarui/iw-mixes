package com.itwray.iw.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.external.model.entity.SmsRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外部SMS短信记录表 Mapper 接口
 *
 * @author wray
 * @since 2024-12-24
 */
@Mapper
public interface SmsRecordsMapper extends BaseMapper<SmsRecordsEntity> {

}
