package com.itwray.iw.starter.rocketmq.web.mapper;

import com.itwray.iw.starter.rocketmq.web.entity.BaseMqConsumeRecordsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * MQ消息消费记录表 Mapper 接口
 *
 * @author wray
 * @since 2025-02-10
 */
@Mapper
public interface BaseMqConsumeRecordsMapper extends BaseMapper<BaseMqConsumeRecordsEntity> {

}
