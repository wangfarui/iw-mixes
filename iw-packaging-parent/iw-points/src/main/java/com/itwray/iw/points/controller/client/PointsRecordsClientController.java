package com.itwray.iw.points.controller.client;

import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.service.PointsRecordsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分记录 Client控制层
 *
 * @author wray
 * @since 2024/9/26
 */
@RestController
@RequestMapping("/client/records")
@Validated
public class PointsRecordsClientController {

    private final PointsRecordsService pointsRecordsService;

    @Autowired
    public PointsRecordsClientController(PointsRecordsService pointsRecordsService) {
        this.pointsRecordsService = pointsRecordsService;
    }

    @PostMapping("/add")
    @Operation(summary = "新增积分记录")
    public Integer add(@RequestBody @Valid PointsRecordsAddDto dto) {
        return pointsRecordsService.add(dto);
    }

}
