package com.itwray.iw.starter.rocketmq.web.mapper;

import com.itwray.iw.starter.rocketmq.web.entity.BaseMqProduceRecordsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * MQ消息生产记录表 Mapper 接口
 *
 * @author wray
 * @since 2025-02-11
 */
@Mapper
public interface BaseMqProduceRecordsMapper extends BaseMapper<BaseMqProduceRecordsEntity> {

}
