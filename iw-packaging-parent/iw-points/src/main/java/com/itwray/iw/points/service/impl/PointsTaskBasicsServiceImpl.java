package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.mapper.PointsTaskBasicsMapper;
import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateStatusDto;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsListVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务基础表 服务实现类
 *
 * @author wray
 * @since 2025-03-19
 */
@Service
public class PointsTaskBasicsServiceImpl extends WebServiceImpl<PointsTaskBasicsDao, PointsTaskBasicsMapper, PointsTaskBasicsEntity,
        TaskBasicsAddDto, TaskBasicsUpdateDto, TaskBasicsDetailVo, Integer> implements PointsTaskBasicsService {

    public PointsTaskBasicsServiceImpl(PointsTaskBasicsDao baseDao) {
        super(baseDao);
    }

    @Override
    public List<TaskBasicsListVo> queryList(TaskBasicsListDto dto) {
        return getBaseDao().lambdaQuery()
                .eq(dto.getTaskGroupId() != null, PointsTaskBasicsEntity::getTaskGroupId, dto.getTaskGroupId())
                .eq(dto.getParentId() != null, PointsTaskBasicsEntity::getParentId, dto.getParentId())
                .ge(dto.getStartDeadlineDate() != null, PointsTaskBasicsEntity::getDeadlineDate, dto.getStartDeadlineDate())
                .le(dto.getEndDeadlineDate() != null, PointsTaskBasicsEntity::getDeadlineDate, dto.getEndDeadlineDate())
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT)
                .orderByDesc(PointsTaskBasicsEntity::getSort)
                .orderByDesc(PointsTaskBasicsEntity::getId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, TaskBasicsListVo.class))
                .toList();
    }

    @Override
    public void updateTaskStatus(TaskBasicsUpdateStatusDto dto) {
        getBaseDao().queryById(dto.getId());
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getId, dto.getId())
                .set(PointsTaskBasicsEntity::getTaskStatus, dto.getTaskStatus())
                .set(TaskStatusEnum.DONE.equals(dto.getTaskStatus()), PointsTaskBasicsEntity::getDoneTime, LocalDateTime.now())
                .set(PointsTaskBasicsEntity::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public List<TaskBasicsListVo> doneList(Integer taskGroupId, Integer currentPage) {
        return getBaseDao().lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DONE)
                .eq(taskGroupId != null, PointsTaskBasicsEntity::getTaskGroupId, taskGroupId)
                .orderByDesc(PointsTaskBasicsEntity::getUpdateTime)
                // 默认每次只查10条数据
                .last(WebCommonConstants.standardPageLimit(currentPage, 10))
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, TaskBasicsListVo.class))
                .toList();
    }

    @Override
    public List<TaskBasicsListVo> deletedList(Boolean more) {
        return getBaseDao().lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DELETED)
                .orderByDesc(PointsTaskBasicsEntity::getUpdateTime)
                // 默认只查最近20条数据
                .last(!Boolean.TRUE.equals(more), WebCommonConstants.standardLimit(20))
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, TaskBasicsListVo.class))
                .toList();
    }

    @Override
    public void clearDeletedList() {
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DELETED)
                .remove();
    }
}
