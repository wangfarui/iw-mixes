package com.itwray.iw.external.mapper;

import com.itwray.iw.external.model.entity.ExternalExchangeRateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货币汇率表 Mapper 接口
 *
 * @author wray
 * @since 2025-04-12
 */
@Mapper
public interface ExternalExchangeRateMapper extends BaseMapper<ExternalExchangeRateEntity> {

}
