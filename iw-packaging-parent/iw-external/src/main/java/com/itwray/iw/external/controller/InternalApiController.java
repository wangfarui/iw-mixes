package com.itwray.iw.external.controller;

import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.vo.GetExchangeRateVo;
import com.itwray.iw.external.service.InternalApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部API接口
 *
 * @author wray
 * @since 2025/4/12
 */
@RestController
@RequestMapping(ExternalClientConstants.INTERNAL_PATH_PREFIX + "/api")
@Tag(name = "内部API接口（内部服务使用）")
public class InternalApiController {

    private final InternalApiService internalApiService;

    public InternalApiController(InternalApiService internalApiService) {
        this.internalApiService = internalApiService;
    }

    @PostMapping("/getExchangeRate")
    @Operation(summary = "查询汇率")
    public GetExchangeRateVo getExchangeRate(@RequestBody @Valid GetExchangeRateDto dto) {
        return internalApiService.getExchangeRate(dto);
    }
}
