package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateStatusDto;
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

    @PutMapping("/updateStatus")
    @Operation(summary = "更新任务状态")
    public void updateTaskStatus(@RequestBody TaskBasicsUpdateStatusDto dto) {
        getWebService().updateTaskStatus(dto);
    }

    @GetMapping("/doneList")
    @Operation(summary = "查询已完成的任务列表")
    public List<TaskBasicsListVo> doneList(@RequestParam(value = "taskGroupId", required = false) Integer taskGroupId,
                                           @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        return getWebService().doneList(taskGroupId, currentPage);
    }

    @GetMapping("/deletedList")
    @Operation(summary = "查询垃圾箱（已删除的任务列表）")
    public List<TaskBasicsListVo> deletedList(@RequestParam(value = "more", required = false) Boolean more) {
        return getWebService().deletedList(more);
    }

    @DeleteMapping("/clearDeletedList")
    @Operation(summary = "清空垃圾箱")
    public void clearDeletedList() {
        getWebService().clearDeletedList();
    }

}
