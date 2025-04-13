package com.itwray.iw.external.service;

import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.vo.GetExchangeRateVo;

/**
 * 内部接口服务
 *
 * @author wray
 * @since 2025/4/12
 */
public interface InternalApiService {

    GetExchangeRateVo getExchangeRate(GetExchangeRateDto dto);
}
