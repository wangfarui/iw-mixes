package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.points.dao.PointsTaskBaseDao;
import com.itwray.iw.points.dao.PointsTaskOnceDao;
import com.itwray.iw.points.dao.PointsTaskPeriodicDao;
import com.itwray.iw.points.dao.PointsTaskRecordsDao;
import com.itwray.iw.points.mapper.PointsTaskBaseMapper;
import com.itwray.iw.points.model.bo.PointsTaskFullBo;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.PointsTaskAddDto;
import com.itwray.iw.points.model.dto.PointsTaskSubmitDto;
import com.itwray.iw.points.model.dto.PointsTaskUpdateDto;
import com.itwray.iw.points.model.entity.PointsTaskBaseEntity;
import com.itwray.iw.points.model.entity.PointsTaskOnceEntity;
import com.itwray.iw.points.model.entity.PointsTaskPeriodicEntity;
import com.itwray.iw.points.model.entity.PointsTaskRecordsEntity;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTaskTypeEnum;
import com.itwray.iw.points.model.vo.PointsTaskDetailVo;
import com.itwray.iw.points.model.vo.PointsTaskListVo;
import com.itwray.iw.points.service.PointsTaskService;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.constants.MQTopicConstants;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分记录 服务实现层
 *
 * @author wray
 * @since 2024/9/26
 */
@Service
public class PointsTaskServiceImpl extends WebServiceImpl<PointsTaskBaseMapper, PointsTaskBaseEntity, PointsTaskBaseDao,
        PointsTaskAddDto, PointsTaskUpdateDto, PointsTaskDetailVo, Integer> implements PointsTaskService {

    private final PointsTaskOnceDao pointsTaskOnceDao;

    private final PointsTaskPeriodicDao pointsTaskPeriodicDao;

    private final PointsTaskRecordsDao pointsTaskRecordsDao;

    @Autowired
    public PointsTaskServiceImpl(PointsTaskBaseDao baseDao, PointsTaskOnceDao pointsTaskOnceDao,
                                 PointsTaskPeriodicDao pointsTaskPeriodicDao, PointsTaskRecordsDao pointsTaskRecordsDao) {
        super(baseDao);
        this.pointsTaskOnceDao = pointsTaskOnceDao;
        this.pointsTaskPeriodicDao = pointsTaskPeriodicDao;
        this.pointsTaskRecordsDao = pointsTaskRecordsDao;
    }

    @Override
    @Transactional
    public Integer add(PointsTaskAddDto dto) {
        // 保存任务基础实体
        PointsTaskBaseEntity taskBaseEntity = BeanUtil.copyProperties(dto, PointsTaskBaseEntity.class);
        getBaseDao().save(taskBaseEntity);

        // 新增任务类型子实体
        this.saveTaskTypeEntity(dto, taskBaseEntity.getId());

        return taskBaseEntity.getId();
    }

    @Override
    @Transactional
    public void update(PointsTaskUpdateDto dto) {
        PointsTaskBaseEntity historyBaseEntity = getBaseDao().queryById(dto.getId());
        // 如果修改后的任务类型与原类型不匹配
        if (!dto.getTaskType().equals(historyBaseEntity.getTaskType())) {
            // 删除历史任务类型下的子表数据
            switch (historyBaseEntity.getTaskType()) {
                case ONCE -> pointsTaskOnceDao.removeById(historyBaseEntity.getId());
                case PERIODIC -> pointsTaskPeriodicDao.removeById(historyBaseEntity.getId());
            }
        }

        // 更新基础实体
        PointsTaskBaseEntity taskBaseEntity = BeanUtil.copyProperties(dto, PointsTaskBaseEntity.class);
        getBaseDao().updateById(taskBaseEntity);

        // 新增任务类型子实体
        this.saveTaskTypeEntity(dto, taskBaseEntity.getId());
    }

    @Override
    public PointsTaskDetailVo detail(Integer id) {
        PointsTaskFullBo pointsTaskFullBo = getBaseDao().queryOneById(id);
        return BeanUtil.copyProperties(pointsTaskFullBo, PointsTaskDetailVo.class);
    }

    @Override
    @Transactional
    public void submit(PointsTaskSubmitDto dto) {
        // 查询任务基础实体
        PointsTaskFullBo taskFullBo = getBaseDao().queryOneById(dto.getId());

        // 计算变动的积分量
        Integer points;
        // 如果允许自定义积分 并且 自定义积分值不为空
        if (Boolean.TRUE.equals(taskFullBo.getAllowCustomPoints()) && dto.getCustomPoints() != null) {
            // 使用自定义积分
            points = dto.getCustomPoints();
        } else {
            // 如果是一次性任务 并且 当前时间 > 一次性任务的截止时间
            if (PointsTaskTypeEnum.ONCE.equals(taskFullBo.getTaskType()) && LocalDateTime.now().compareTo(taskFullBo.getDeadline()) > 0) {
                // 积分为0，表示不加积分
                points = 0;
            } else {
                // 默认为基础积分
                points = taskFullBo.getBasePoints();
            }
        }

        // 新增任务执行记录
        PointsTaskRecordsEntity taskRecordsEntity = new PointsTaskRecordsEntity();
        taskRecordsEntity.setTaskId(taskFullBo.getId());
        taskRecordsEntity.setRecordTime(LocalDateTime.now());
        taskRecordsEntity.setActualPoints(points);
        pointsTaskRecordsDao.save(taskRecordsEntity);

        // 如果变动积分不为0，则同步用户积分
        if (points != 0) {
            PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
            pointsRecordsAddDto.setPoints(points);
            pointsRecordsAddDto.setSource(taskFullBo.getName());
            pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.POINTS_TASK_MANUAL.getCode());
            pointsRecordsAddDto.setUserId(UserUtils.getUserId());
            MQProducerHelper.send(MQTopicConstants.POINTS_RECORDS, pointsRecordsAddDto);
        }
    }

    /**
     * 查询当前用户的任务列表
     *
     * @return 任务列表
     */
    @Override
    public List<PointsTaskListVo> list() {
        return getBaseDao().list().stream().map(t -> {
            PointsTaskListVo vo = new PointsTaskListVo();
            vo.setId(t.getId());
            vo.setTaskName(t.getName());
            vo.setTaskPoints(t.getBasePoints());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 保存任务类型子实体
     *
     * @param dto 任务对象
     * @param id  任务基础实体id
     */
    private void saveTaskTypeEntity(PointsTaskAddDto dto, Integer id) {
        switch (dto.getTaskType()) {
            case ONCE -> {
                PointsTaskOnceEntity taskOnceEntity = BeanUtil.copyProperties(dto, PointsTaskOnceEntity.class);
                taskOnceEntity.setId(id);
                pointsTaskOnceDao.save(taskOnceEntity);
            }
            case PERIODIC -> {
                PointsTaskPeriodicEntity taskPeriodicEntity = BeanUtil.copyProperties(dto, PointsTaskPeriodicEntity.class);
                taskPeriodicEntity.setId(id);
                pointsTaskPeriodicDao.save(taskPeriodicEntity);
            }
        }
    }
}
