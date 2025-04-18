package com.itwray.iw.points.service;

import com.itwray.iw.web.service.WebService;
import com.itwray.iw.points.model.dto.PointsTaskRelationAddDto;
import com.itwray.iw.points.model.dto.PointsTaskRelationUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskRelationDetailVo;

/**
 * 任务关联表 服务接口
 *
 * @author wray
 * @since 2025-04-17
 */
public interface PointsTaskRelationService extends WebService<PointsTaskRelationAddDto, PointsTaskRelationUpdateDto, PointsTaskRelationDetailVo, Integer> {

    Integer save(PointsTaskRelationAddDto dto);

    PointsTaskRelationDetailVo getByTaskId(Integer taskId);
}
