package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.PointsTaskGroupAddDto;
import com.itwray.iw.points.model.dto.PointsTaskGroupUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskGroupDetailVo;
import com.itwray.iw.web.service.WebService;

/**
 * 任务分组表 服务接口
 *
 * @author wray
 * @since 2025-03-19
 */
public interface PointsTaskGroupService extends WebService<PointsTaskGroupAddDto, PointsTaskGroupUpdateDto, PointsTaskGroupDetailVo, Integer> {

}
