package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.dao.PointsTaskGroupDao;
import com.itwray.iw.points.dao.PointsTaskRelationDao;
import com.itwray.iw.points.mapper.PointsTaskBasicsMapper;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateStatusDto;
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
import com.itwray.iw.web.model.enums.mq.PointsRecordsTopicEnum;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PointsTaskBasicsServiceImpl(PointsTaskBasicsDao baseDao, PointsTaskGroupDao pointsTaskGroupDao, PointsTaskRelationDao pointsTaskRelationDao) {
        super(baseDao);
        this.pointsTaskGroupDao = pointsTaskGroupDao;
        this.pointsTaskRelationDao = pointsTaskRelationDao;
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
                .orderByDesc(PointsTaskBasicsEntity::getSort)
                .orderByDesc(PointsTaskBasicsEntity::getId)
                .list();
        return this.buildListVo(entityList);
    }

    @Override
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
            if (taskRelationEntity != null) {
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
    public void clearDeletedList() {
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DELETED)
                .remove();
    }

    @Override
    public TaskBasicsDetailVo detail(Integer integer) {
        TaskBasicsDetailVo vo = super.detail(integer);
        PointsTaskRelationEntity taskRelationEntity = pointsTaskRelationDao.getByTaskId(vo.getId());
        if (taskRelationEntity != null) {
            vo.setRewardPoints(taskRelationEntity.getRewardPoints());
            vo.setPunishPoints(taskRelationEntity.getPunishPoints());
        }
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
