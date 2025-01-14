package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.PointsTaskAddDto;
import com.itwray.iw.points.model.dto.PointsTaskSubmitDto;
import com.itwray.iw.points.model.dto.PointsTaskUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskDetailVo;
import com.itwray.iw.points.model.vo.PointsTaskListVo;
import com.itwray.iw.points.service.PointsTaskService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 积分任务 接口控制层
 *
 * @author wray
 * @since 2024/9/26
 */
@RestController
@RequestMapping("/task")
@Validated
@Tag(name = "积分任务接口")
public class PointsTaskController extends WebController<PointsTaskService, PointsTaskAddDto,
        PointsTaskUpdateDto, PointsTaskDetailVo, Integer> {

    @Autowired
    public PointsTaskController(PointsTaskService webService) {
        super(webService);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交积分任务")
    public void submit(@RequestBody PointsTaskSubmitDto dto) {
        getWebService().submit(dto);
    }

    @GetMapping("/list")
    @Operation(summary = "查询积分任务列表")
    public List<PointsTaskListVo> list() {
        return getWebService().list();
    }
}
