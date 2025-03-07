package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.PointsTaskAddDto;
import com.itwray.iw.points.model.dto.PointsTaskSubmitDto;
import com.itwray.iw.points.model.dto.PointsTaskUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskDetailVo;
import com.itwray.iw.points.model.vo.PointsTaskListVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 积分任务表 服务接口层
 *
 * @author wray
 * @since 2024/9/26
 */
public interface PointsTaskService extends WebService<PointsTaskAddDto, PointsTaskUpdateDto, PointsTaskDetailVo, Integer> {

    /**
     * 提交积分任务
     *
     * @param dto 任务提交对象
     */
    void submit(PointsTaskSubmitDto dto);

    /**
     * 查询积分任务列表
     *
     * @return 任务列表
     */
    List<PointsTaskListVo> list();
}
