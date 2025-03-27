package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.task.TaskGroupAddDto;
import com.itwray.iw.points.model.dto.task.TaskGroupUpdateDto;
import com.itwray.iw.points.model.vo.task.FixedGroupTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskGroupDetailVo;
import com.itwray.iw.points.model.vo.task.TaskGroupListVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 任务分组表 服务接口
 *
 * @author wray
 * @since 2025-03-19
 */
public interface PointsTaskGroupService extends WebService<TaskGroupAddDto, TaskGroupUpdateDto, TaskGroupDetailVo, Integer> {

    List<TaskGroupListVo> queryListByParentId(Integer parentId);

    FixedGroupTaskNumVo statisticsLatestTaskNum();

}
