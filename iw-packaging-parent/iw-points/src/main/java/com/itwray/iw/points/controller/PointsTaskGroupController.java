package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.task.TaskGroupAddDto;
import com.itwray.iw.points.model.dto.task.TaskGroupUpdateDto;
import com.itwray.iw.points.model.vo.task.StatisticsLatestTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskGroupDetailVo;
import com.itwray.iw.points.model.vo.task.TaskGroupListVo;
import com.itwray.iw.points.service.PointsTaskGroupService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务分组表 接口控制层
 *
 * @author wray
 * @since 2025-03-19
 */
@RestController
@RequestMapping("/task/group")
@Validated
@Tag(name = "任务分组表接口")
public class PointsTaskGroupController extends WebController<PointsTaskGroupService,
        TaskGroupAddDto, TaskGroupUpdateDto, TaskGroupDetailVo, Integer> {

    public PointsTaskGroupController(PointsTaskGroupService webService) {
        super(webService);
    }

    @GetMapping("/list")
    @Operation(summary = "查询积分任务列表")
    public List<TaskGroupListVo> list(@RequestParam(value = "parentId", required = false) Integer parentId) {
        return getWebService().queryListByParentId(parentId);
    }

    @GetMapping("/statisticsLatestTaskNum")
    @Operation(summary = "统计最近的任务数量")
    public StatisticsLatestTaskNumVo statisticsLatestTaskNum() {
        return getWebService().statisticsLatestTaskNum();
    }
}
