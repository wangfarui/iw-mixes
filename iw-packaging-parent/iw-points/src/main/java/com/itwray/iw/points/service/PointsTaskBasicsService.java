package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateStatusDto;
import com.itwray.iw.points.model.vo.task.FixedGroupTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsListVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 任务基础表 服务接口
 *
 * @author wray
 * @since 2025-03-19
 */
public interface PointsTaskBasicsService extends WebService<TaskBasicsAddDto, TaskBasicsUpdateDto, TaskBasicsDetailVo, Integer> {

    List<TaskBasicsListVo> queryList(TaskBasicsListDto dto);

    void updateTaskStatus(TaskBasicsUpdateStatusDto dto);

    List<TaskBasicsListVo> doneList(Integer taskGroupId, Boolean more);

    List<TaskBasicsListVo> deletedList(Boolean more);
}
