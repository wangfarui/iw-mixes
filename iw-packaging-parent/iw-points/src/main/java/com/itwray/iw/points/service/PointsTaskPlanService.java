package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.plan.PointsTaskPlanAddDto;
import com.itwray.iw.points.model.dto.plan.PointsTaskPlanPageDto;
import com.itwray.iw.points.model.dto.plan.PointsTaskPlanUpdateDto;
import com.itwray.iw.points.model.vo.plan.PointsTaskPlanDetailVo;
import com.itwray.iw.points.model.vo.plan.PointsTaskPlanPageVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

/**
 * 任务计划表 服务接口
 *
 * @author wray
 * @since 2025-05-07
 */
public interface PointsTaskPlanService extends WebService<PointsTaskPlanAddDto, PointsTaskPlanUpdateDto, PointsTaskPlanDetailVo, Integer> {

    PageVo<PointsTaskPlanPageVo> page(PointsTaskPlanPageDto dto);
}
