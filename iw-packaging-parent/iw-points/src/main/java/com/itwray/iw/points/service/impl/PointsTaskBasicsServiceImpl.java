package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.mapper.PointsTaskBasicsMapper;
import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsListDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsUpdateStatusDto;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.points.model.vo.task.FixedGroupTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsDetailVo;
import com.itwray.iw.points.model.vo.task.TaskBasicsListVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;

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
                .eq(PointsTaskBasicsEntity::getTaskGroupId, dto.getTaskGroupId())
                .eq(dto.getParentId() != null, PointsTaskBasicsEntity::getParentId, dto.getParentId())
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT.getCode())
                .orderByDesc(PointsTaskBasicsEntity::getSort)
                .orderByDesc(PointsTaskBasicsEntity::getId)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, TaskBasicsListVo.class))
                .toList();
    }

    @Override
    public FixedGroupTaskNumVo statisticsFixedGroupTaskNum() {
        return null;
    }

    @Override
    public void updateTaskStatus(TaskBasicsUpdateStatusDto dto) {
        if (!ConstantEnumUtil.isEnumCode(TaskStatusEnum.class, dto.getTaskStatus())) {
            throw new BusinessException("任务状态异常");
        }
        getBaseDao().queryById(dto.getId());
        getBaseDao().lambdaUpdate()
                .eq(PointsTaskBasicsEntity::getId, dto.getId())
                .set(PointsTaskBasicsEntity::getTaskStatus, dto.getTaskStatus())
                .update();
    }

    @Override
    public List<TaskBasicsListVo> doneList() {
        return getBaseDao().lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.DONE.getCode())
                .orderByDesc(PointsTaskBasicsEntity::getUpdateTime)
                .list()
                .stream()
                .map(t -> BeanUtil.copyProperties(t, TaskBasicsListVo.class))
                .toList();
    }
}
