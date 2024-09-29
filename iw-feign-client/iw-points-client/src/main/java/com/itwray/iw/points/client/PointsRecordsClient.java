package com.itwray.iw.points.client;

import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Points服务 - 积分记录Client
 *
 * @author wray
 * @since 2024/9/26
 */
@FeignClient(value = "iw-points-service", path = "/points-service/client/records")
public interface PointsRecordsClient {

    @PostMapping("/add")
    @Operation(summary = "新增积分记录")
    Integer add(@RequestBody @Valid PointsRecordsAddDto dto);
}
