package com.itwray.iw.external.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.external.service.ExternalApiService;
import com.itwray.iw.web.annotation.SkipWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 外部API的接口控制层
 *
 * @author wray
 * @since 2024/10/17
 */
@RestController
@RequestMapping("/api")
@Validated
@Tag(name = "外部API接口")
@SkipWrapper
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @Autowired
    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/heartbeat")
    @Operation(summary = "心跳接口")
    public void heartbeat() {
        externalApiService.heartbeat();
    }

    @GetMapping("/getWeather")
    @Operation(summary = "查询天气")
    public Map<Object, Object> getWeather() {
        return externalApiService.getWeather();
    }

    @GetMapping("/system/getWeather")
    @Operation(summary = "系统查询天气")
    public GeneralResponse<Map<Object, Object>> getWeatherBySystem() {
        return GeneralResponse.success(externalApiService.getWeather());
    }

    @PostMapping("/getMonitors")
    @Operation(summary = "查询站点监测情况")
    public Map<Object, Object> getMonitors(@RequestBody Map<String, Object> bodyParam) {
        return externalApiService.getMonitorsByUptimeRobot(bodyParam);
    }

    @GetMapping("/getDailyHot/{source}")
    @Operation(summary = "查询每日热点")
    public Map<Object, Object> getDailyHot(@PathVariable String source) {
        return externalApiService.getDailyHot(source);
    }

}
