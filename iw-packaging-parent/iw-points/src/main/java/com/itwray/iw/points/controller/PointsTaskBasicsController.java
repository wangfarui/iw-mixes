package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.vo.task.FixedGroupTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsListVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务基础表 接口控制层
 *
 * @author wray
 * @since 2025-03-19
 */
@RestController
@RequestMapping("/task/basics")
@Validated
@Tag(name = "任务基础表接口")
public class PointsTaskBasicsController extends WebController<PointsTaskBasicsService,
        TaskBasicsAddDto, TaskBasicsUpdateDto, TaskBasicsDetailVo, Integer> {

    public PointsTaskBasicsController(PointsTaskBasicsService webService) {
        super(webService);
    }

    @PostMapping("/list")
    @Operation(summary = "查询任务列表")
    public List<TaskBasicsListVo> list(@RequestBody TaskBasicsListDto dto) {
        return getWebService().queryList(dto);
    }

    @GetMapping("/statisticsFixedGroupTaskNum")
    @Operation(summary = "统计固定分组任务数量")
    public FixedGroupTaskNumVo statisticsFixedGroupTaskNum() {
        return getWebService().statisticsFixedGroupTaskNum();
    }
}
