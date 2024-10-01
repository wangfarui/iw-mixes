package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.PointsRecordsPageDto;
import com.itwray.iw.points.model.dto.PointsRecordsStatisticsDto;
import com.itwray.iw.points.model.dto.PointsRecordsUpdateDto;
import com.itwray.iw.points.model.vo.PointsRecordsDetailVo;
import com.itwray.iw.points.model.vo.PointsRecordsPageVo;
import com.itwray.iw.points.model.vo.PointsRecordsStatisticsVo;
import com.itwray.iw.points.service.PointsRecordsService;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分记录 接口控制层
 *
 * @author wray
 * @since 2024/9/26
 */
@RestController
@RequestMapping("/records")
@Validated
@Tag(name = "积分记录接口")
public class PointsRecordsController extends WebController<PointsRecordsService, PointsRecordsAddDto, PointsRecordsUpdateDto, PointsRecordsDetailVo> {

    @Autowired
    public PointsRecordsController(PointsRecordsService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询积分记录")
    public PageVo<PointsRecordsPageVo> page(@RequestBody @Valid PointsRecordsPageDto dto) {
        return getWebService().page(dto);
    }

    @PostMapping("/statistics")
    @Operation(summary = "查询记账统计信息")
    public PointsRecordsStatisticsVo statistics(@RequestBody PointsRecordsStatisticsDto dto) {
        return getWebService().statistics(dto);
    }
}
