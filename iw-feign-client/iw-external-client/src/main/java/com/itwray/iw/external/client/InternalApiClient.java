package com.itwray.iw.external.client;

import com.itwray.iw.external.core.InternalFeignConfig;
import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.vo.GetExchangeRateVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description
 *
 * @author wray
 * @since 2025/4/12
 */
@Validated
@FeignClient(value = ExternalClientConstants.SERVICE_NAME, contextId = "internal-client", path = ExternalClientConstants.INTERNAL_SERVICE_PATH + "/api", configuration = InternalFeignConfig.class)
public interface InternalApiClient {

    @GetMapping("/getExchangeRate")
    @Operation(summary = "查询汇率")
    GetExchangeRateVo getExchangeRate(@RequestBody @Valid GetExchangeRateDto dto);
}
