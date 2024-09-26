package com.itwray.iw.points.controller;

import com.itwray.iw.points.service.PointsTotalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分合计 接口控制层
 *
 * @author wray
 * @since 2024/9/26
 */
@RestController
@RequestMapping("/total")
@Validated
@Tag(name = "积分合计接口")
public class PointsTotalController {

    private final PointsTotalService pointsTotalService;

    @Autowired
    public PointsTotalController(PointsTotalService pointsTotalService) {
        this.pointsTotalService = pointsTotalService;
    }

    @GetMapping("/getPointsBalance")
    @Operation(summary = "获取积分余额")
    public Integer getPointsBalance() {
        return pointsTotalService.getPointsBalance();
    }
}
