package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务基础表 接口控制层
 *
 * @author wray
 * @since 2025-03-19
 */
@RestController
@RequestMapping("/taskBasics")
@Validated
@Tag(name = "任务基础表接口")
public class PointsTaskBasicsController extends WebController<PointsTaskBasicsService,
        TaskBasicsAddDto, TaskBasicsUpdateDto, TaskBasicsDetailVo, Integer> {

    public PointsTaskBasicsController(PointsTaskBasicsService webService) {
        super(webService);
    }
}
