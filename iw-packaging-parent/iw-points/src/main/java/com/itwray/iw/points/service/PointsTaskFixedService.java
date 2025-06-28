package com.itwray.iw.points.service;

import com.itwray.iw.web.service.WebService;
import com.itwray.iw.points.model.dto.PointsTaskFixedAddDto;
import com.itwray.iw.points.model.dto.PointsTaskFixedUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskFixedDetailVo;

import java.util.List;

/**
 * 常用任务表 服务接口
 *
 * @author wray
 * @since 2025-06-06
 */
public interface PointsTaskFixedService extends WebService<PointsTaskFixedAddDto, PointsTaskFixedUpdateDto, PointsTaskFixedDetailVo, Integer> {

    void submit(Integer id);

    List<PointsTaskFixedDetailVo> list();
}
