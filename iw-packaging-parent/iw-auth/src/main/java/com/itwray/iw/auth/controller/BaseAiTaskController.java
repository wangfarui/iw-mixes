package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.AiTaskAddDto;
import com.itwray.iw.auth.model.dto.AiTaskPageDto;
import com.itwray.iw.auth.model.dto.AiTaskUpdateDto;
import com.itwray.iw.auth.model.vo.AiTaskDetailVo;
import com.itwray.iw.auth.model.vo.AiTaskPageVo;
import com.itwray.iw.auth.service.BaseAiTaskService;
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
 * AI任务接口控制层
 *
 * @author wray
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/ai/task")
@Validated
@Tag(name = "AI任务接口")
public class BaseAiTaskController extends WebController<BaseAiTaskService, AiTaskAddDto, AiTaskUpdateDto, AiTaskDetailVo, Integer> {

    @Autowired
    public BaseAiTaskController(BaseAiTaskService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询AI任务")
    public PageVo<AiTaskPageVo> page(@RequestBody @Valid AiTaskPageDto dto) {
        return getWebService().page(dto);
    }
}
