package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.plan.PointsTaskPlanAddDto;
import com.itwray.iw.points.model.dto.plan.PointsTaskPlanPageDto;
import com.itwray.iw.points.model.dto.plan.PointsTaskPlanUpdateDto;
import com.itwray.iw.points.model.vo.plan.PointsTaskPlanDetailVo;
import com.itwray.iw.points.model.vo.plan.PointsTaskPlanPageVo;
import com.itwray.iw.points.service.PointsTaskPlanService;
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
 * 任务计划表 接口控制层
 *
 * @author wray
 * @since 2025-05-07
 */
@RestController
@RequestMapping("/task/plan")
@Validated
@Tag(name = "任务计划表接口")
public class PointsTaskPlanController extends WebController<PointsTaskPlanService,
        PointsTaskPlanAddDto, PointsTaskPlanUpdateDto, PointsTaskPlanDetailVo, Integer>  {

    @Autowired
    public PointsTaskPlanController(PointsTaskPlanService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询任务计划")
    public PageVo<PointsTaskPlanPageVo> page(@RequestBody @Valid PointsTaskPlanPageDto dto) {
        return getWebService().page(dto);
    }
}
