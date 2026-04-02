package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.AiTaskAddDto;
import com.itwray.iw.auth.model.dto.AiTaskPageDto;
import com.itwray.iw.auth.model.dto.AiTaskUpdateDto;
import com.itwray.iw.auth.model.vo.AiTaskDetailVo;
import com.itwray.iw.auth.model.vo.AiTaskPageVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

/**
 * AI任务服务接口
 *
 * @author wray
 * @since 2026-03-26
 */
public interface BaseAiTaskService extends WebService<AiTaskAddDto, AiTaskUpdateDto, AiTaskDetailVo, Integer> {

    PageVo<AiTaskPageVo> page(AiTaskPageDto dto);
}
