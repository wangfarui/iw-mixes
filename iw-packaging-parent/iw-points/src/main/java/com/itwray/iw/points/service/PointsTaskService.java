package com.itwray.iw.points.service;

import com.itwray.iw.points.model.vo.PointsTaskListVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 积分任务表 接口服务层
 *
 * @author wray
 * @since 2024/9/26
 */
public interface PointsTaskService extends WebService {

    /**
     * 提交积分任务
     *
     * @param id 积分任务id
     */
    void submit(Integer id);

    /**
     * 查询积分任务列表
     *
     * @return 任务列表
     */
    List<PointsTaskListVo> list();
}
