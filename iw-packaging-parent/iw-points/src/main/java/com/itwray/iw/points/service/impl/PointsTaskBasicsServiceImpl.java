package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.dao.PointsTaskGroupDao;
import com.itwray.iw.points.dao.PointsTaskRelationDao;
import com.itwray.iw.points.mapper.PointsTaskBasicsMapper;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.task.*;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.entity.PointsTaskRelationEntity;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsListVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseBusinessFileDao;
import com.itwray.iw.web.model.enums.BusinessFileTypeEnum;
import com.itwray.iw.web.model.enums.mq.PointsRecordsTopicEnum;
import com.itwray.iw.web.model.vo.FileVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 任务基础表 服务实现类
 *
 * @author wray
 * @since 2025-03-19
 */
@Service
public class PointsTaskBasicsServiceImpl extends WebServiceImpl<PointsTaskBasicsDao, PointsTaskBasicsMapper, PointsTaskBasicsEntity,
        TaskBasicsAddDto, TaskBasicsUpdateDto, TaskBasicsDetailVo, Integer> implements PointsTaskBasicsService {

    private final PointsTaskGroupDao pointsTaskGroupDao;

    private final PointsTaskRelationDao pointsTaskRelationDao;

    private BaseBusinessFileDao baseBusinessFileDao;

    @Autowired
    public PointsTaskBasicsServiceImpl(PointsTaskBasicsDao baseDao, PointsTaskGroupDao pointsTaskGroupDao, PointsTaskRelationDao pointsTaskRelationDao) {
        super(baseDao);
        this.pointsTaskGroupDao = pointsTaskGroupDao;
        this.pointsTaskRelationDao = pointsTaskRelationDao;
    }

    @Autowired
    public void setBaseBusinessFileDao(BaseBusinessFileDao baseBusinessFileDao) {
        this.baseBusinessFileDao = baseBusinessFileDao;
    }

    @Override
    @Transactional
    public Integer add(TaskBasicsAddDto dto) {
        dto.setSort(getBaseDao().queryMaxSortByGroupId(dto.getTaskGroupId()));
        return super.add(dto);
    }

    @Override
    public List<TaskBasicsListVo> queryList(TaskBasicsListDto dto) {
        List<PointsTaskBasicsEntity> entityList = getBaseDao().lambdaQuery()
                .eq(dto.getTaskGroupId() != null, PointsTaskBasicsEntity::getTaskGroupId, dto.getTaskGroupId())
                .eq(dto.getParentId() != null, PointsTaskBasicsEntity::getParentId, dto.getParentId())
                .ge(dto.getStartDeadlineDate() != null, PointsTaskBasicsEntity::getDeadlineDate, dto.getStartDeadlineDate())
                .le(dto.getEndDeadlineDate() != null, PointsTaskBasicsEntity::getDeadlineDate, dto.getEndDeadlineDate())
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT)
                .orderByAsc(Boolean.TRUE.equals(dto.getSortDeadline()), PointsTaskBasicsEntity::getDeadlineDate)
                .orderByDesc(PointsTaskBasicsEntity::getSort)
                .orderByDesc(PointsTaskBasicsEntity::getId)
                .list();
        return this.buildListVo(entityList);
    }

    @Override
    @Transactional
    public void updateTaskStatus(TaskBasicsUpdateStatusDto dto) {
        PointsTaskBasicsEntity taskBasicsEntity = getBaseDao().queryById(dto.getId());
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getId, dto.getId())
                .set(PointsTaskBasicsEntity::getTaskStatus, dto.getTaskStatus())
                .set(TaskStatusEnum.DONE.equals(dto.getTaskStatus()), PointsTaskBasicsEntity::getDoneTime, LocalDateTime.now())
                .set(PointsTaskBasicsEntity::getUpdateTime, LocalDateTime.now())
                .update();

        // 如果是完成任务操作
        if (TaskStatusEnum.DONE.equals(dto.getTaskStatus())) {
            PointsTaskRelationEntity taskRelationEntity = pointsTaskRelationDao.getByTaskId(taskBasicsEntity.getId());
            if (taskRelationEntity != null && (taskBasicsEntity.getDeadlineDate() == null || !LocalDate.now().isAfter(taskBasicsEntity.getDeadlineDate()))) {
                this.syncPoints(taskBasicsEntity, taskRelationEntity.getRewardPoints(), true);
            }
        } else {
            // 如果完成任务后又取消完成
            if (TaskStatusEnum.DONE.equals(taskBasicsEntity.getTaskStatus())) {
                PointsTaskRelationEntity taskRelationEntity = pointsTaskRelationDao.getByTaskId(taskBasicsEntity.getId());
                if (taskRelationEntity != null) {
                    this.syncPoints(taskBasicsEntity, taskRelationEntity.getRewardPoints(), false);
                }
            }
        }
    }

    private void syncPoints(PointsTaskBasicsEntity taskBasicsEntity, Integer points, boolean isFinish) {
        if (points == null || points == 0) {
            return;
        }
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(isFinish ? PointsTransactionTypeEnum.INCREASE.getCode() : PointsTransactionTypeEnum.DEDUCT.getCode());
        pointsRecordsAddDto.setPoints(isFinish ? points : -points);
        pointsRecordsAddDto.setSource((isFinish ? "完成任务" : "取消任务") + "[" + taskBasicsEntity.getId() + "]" + taskBasicsEntity.getTaskName());
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.POINTS_TASK_MANUAL.getCode());
        pointsRecordsAddDto.setUserId(taskBasicsEntity.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.TASK, pointsRecordsAddDto);
    }

    @Override
    public List<TaskBasicsListVo> doneList(Integer taskGroupId, Integer currentPage) {
        List<PointsTaskBasicsEntity> entityList = getBaseDao().lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DONE)
                .eq(taskGroupId != null, PointsTaskBasicsEntity::getTaskGroupId, taskGroupId)
                .orderByDesc(PointsTaskBasicsEntity::getUpdateTime)
                // 默认每次只查10条数据
                .last(WebCommonConstants.standardPageLimit(currentPage, 10))
                .list();

        return this.buildListVo(entityList);
    }

    @Override
    @Transactional
    public List<TaskBasicsListVo> deletedList(Boolean more) {
        List<PointsTaskBasicsEntity> entityList = getBaseDao().lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DELETED)
                .orderByDesc(PointsTaskBasicsEntity::getUpdateTime)
                // 默认只查最近20条数据
                .last(!Boolean.TRUE.equals(more), WebCommonConstants.standardLimit(20))
                .list();

        return this.buildListVo(entityList);
    }

    @Override
    @Transactional
    public void clearDeletedList() {
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DELETED)
                .remove();
    }

    @Override
    @Transactional
    public void addTaskFile(TaskBasicsAddFileDto addFileDto) {
        getBaseDao().queryById(addFileDto.getTaskId());
        baseBusinessFileDao.addBusinessFile(addFileDto.getTaskId(), BusinessFileTypeEnum.POINTS_TASK_BASICS, Collections.singletonList(addFileDto));
    }

    @Override
    public void deleteTaskFile(TaskBasicsDeleteFileDto deleteFileDto) {
        getBaseDao().queryById(deleteFileDto.getTaskId());
        baseBusinessFileDao.removeBusinessFile(deleteFileDto.getTaskId(), BusinessFileTypeEnum.POINTS_TASK_BASICS, deleteFileDto.getFileUrl());
    }

    @Override
    public TaskBasicsDetailVo detail(Integer id) {
        TaskBasicsDetailVo vo = super.detail(id);
        PointsTaskRelationEntity taskRelationEntity = pointsTaskRelationDao.getByTaskId(vo.getId());
        if (taskRelationEntity != null) {
            vo.setRewardPoints(taskRelationEntity.getRewardPoints());
            vo.setPunishPoints(taskRelationEntity.getPunishPoints());
        }
        List<FileVo> fileVoList = baseBusinessFileDao.getBusinessFile(vo.getId(), BusinessFileTypeEnum.POINTS_TASK_BASICS);
        vo.setFileList(fileVoList);
        return vo;
    }

    private List<TaskBasicsListVo> buildListVo(List<PointsTaskBasicsEntity> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<Integer> taskGroupIdList = entityList.stream().map(PointsTaskBasicsEntity::getTaskGroupId).distinct().toList();
        Map<Integer, String> groupNameMap = pointsTaskGroupDao.queryTaskGroupNameMap(taskGroupIdList);
        return entityList.stream()
                .map(t -> {
                    TaskBasicsListVo vo = BeanUtil.copyProperties(t, TaskBasicsListVo.class);
                    vo.setTaskGroupName(groupNameMap.get(vo.getTaskGroupId()));
                    return vo;
                })
                .toList();
    }
}
