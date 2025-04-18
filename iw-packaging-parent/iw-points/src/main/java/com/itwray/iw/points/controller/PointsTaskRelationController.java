package com.itwray.iw.points.controller;

import com.itwray.iw.points.model.dto.PointsTaskRelationAddDto;
import com.itwray.iw.points.model.dto.PointsTaskRelationUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskRelationDetailVo;
import com.itwray.iw.points.service.PointsTaskRelationService;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.exception.IwWebException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务关联表 接口控制层
 *
 * @author wray
 * @since 2025-04-17
 */
@RestController
@RequestMapping("/task/relation")
@Validated
@Tag(name = "任务关联表接口")
public class PointsTaskRelationController extends WebController<PointsTaskRelationService,
        PointsTaskRelationAddDto, PointsTaskRelationUpdateDto, PointsTaskRelationDetailVo, Integer>  {

    @Autowired
    public PointsTaskRelationController(PointsTaskRelationService webService) {
        super(webService);
    }

    @Override
    public Integer add(PointsTaskRelationAddDto dto) {
        throw new IwWebException("不支持的操作");
    }

    @PostMapping("/save")
    public Integer save(@RequestBody @Valid PointsTaskRelationAddDto dto) {
        return getWebService().save(dto);
    }

    @GetMapping("/getByTaskId")
    public PointsTaskRelationDetailVo getByTaskId(@RequestParam("taskId") Integer taskId) {
        return getWebService().getByTaskId(taskId);
    }
}
