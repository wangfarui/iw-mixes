package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.task.*;
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
@RequestMapping("/points/task/basics")
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

    @PostMapping("/addFile")
    @Operation(summary = "任务添加附件")
    public void addTaskFile(@RequestBody TaskBasicsAddFileDto addFileDto) {
        getWebService().addTaskFile(addFileDto);
    }

    @PostMapping("/deleteFile")
    @Operation(summary = "任务删除附件")
    public void deleteTaskFile(@RequestBody TaskBasicsDeleteFileDto deleteFileDto) {
        getWebService().deleteTaskFile(deleteFileDto);
    }

    @PutMapping("/updateTaskParam")
    @Operation(summary = "更新任务参数")
    public void updateTaskParam(@RequestBody TaskBasicsUpdateDto dto) {
        // 只更新不为null的数据, 主要用于更新个别参数
        getWebService().update(dto);
    }
}
