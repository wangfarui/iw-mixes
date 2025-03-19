package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.PointsTaskBasicsAddDto;
import com.itwray.iw.points.model.dto.PointsTaskBasicsUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskBasicsDetailVo;
import com.itwray.iw.web.service.WebService;

/**
 * 任务基础表 服务接口
 *
 * @author wray
 * @since 2025-03-19
 */
public interface PointsTaskBasicsService extends WebService<PointsTaskBasicsAddDto, PointsTaskBasicsUpdateDto, PointsTaskBasicsDetailVo, Integer> {

}
