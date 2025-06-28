package com.itwray.iw.points.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itwray.iw.points.model.dto.PointsTaskFixedAddDto;
import com.itwray.iw.points.model.dto.PointsTaskFixedUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskFixedDetailVo;
import com.itwray.iw.points.service.PointsTaskFixedService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 常用任务表 接口控制层
 *
 * @author wray
 * @since 2025-06-06
 */
@RestController
@RequestMapping("/points/task/fixed")
@Validated
@Tag(name = "常用任务表接口")
public class PointsTaskFixedController extends WebController<PointsTaskFixedService,
        PointsTaskFixedAddDto, PointsTaskFixedUpdateDto, PointsTaskFixedDetailVo, Integer>  {

    @Autowired
    public PointsTaskFixedController(PointsTaskFixedService webService) {
        super(webService);
    }

    @GetMapping("/submit")
    @Operation(summary = "提交任务")
    public void submit(@RequestParam("id") Integer id) {
        getWebService().submit(id);
    }

    @GetMapping("/list")
    @Operation(summary = "任务列表")
    public List<PointsTaskFixedDetailVo> list() {
        return getWebService().list();
    }
}
