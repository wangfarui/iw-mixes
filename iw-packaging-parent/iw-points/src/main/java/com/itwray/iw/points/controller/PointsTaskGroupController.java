package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.PointsTaskGroupAddDto;
import com.itwray.iw.points.model.dto.PointsTaskGroupUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskGroupDetailVo;
import com.itwray.iw.points.service.PointsTaskGroupService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务分组表 接口控制层
 *
 * @author wray
 * @since 2025-03-19
 */
@RestController
@RequestMapping("/taskGroup")
@Validated
@Tag(name = "任务分组表接口")
public class PointsTaskGroupController extends WebController<PointsTaskGroupService,
        PointsTaskGroupAddDto, PointsTaskGroupUpdateDto, PointsTaskGroupDetailVo, Integer> {

    public PointsTaskGroupController(PointsTaskGroupService webService) {
        super(webService);
    }
}
