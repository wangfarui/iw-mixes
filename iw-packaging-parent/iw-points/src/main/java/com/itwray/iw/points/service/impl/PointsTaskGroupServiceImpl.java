package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.common.utils.NumberUtils;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.dao.PointsTaskGroupDao;
import com.itwray.iw.points.mapper.PointsTaskGroupMapper;
import com.itwray.iw.points.model.dto.task.TaskGroupAddDto;
import com.itwray.iw.points.model.dto.task.TaskGroupUpdateDto;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.entity.PointsTaskGroupEntity;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.points.model.vo.task.StatisticsLatestTaskNumVo;
import com.itwray.iw.points.model.vo.task.TaskGroupDetailVo;
import com.itwray.iw.points.model.vo.task.TaskGroupListVo;
import com.itwray.iw.points.service.PointsTaskGroupService;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 任务分组表 服务实现类
 *
 * @author wray
 * @since 2025-03-19
 */
@Service
public class PointsTaskGroupServiceImpl extends WebServiceImpl<PointsTaskGroupMapper, PointsTaskGroupEntity, PointsTaskGroupDao,
        TaskGroupAddDto, TaskGroupUpdateDto, TaskGroupDetailVo, Integer> implements PointsTaskGroupService {

    private final PointsTaskBasicsDao pointsTaskBasicsDao;

    public PointsTaskGroupServiceImpl(PointsTaskGroupDao baseDao, PointsTaskBasicsDao pointsTaskBasicsDao) {
        super(baseDao);
        this.pointsTaskBasicsDao = pointsTaskBasicsDao;
    }

    @Override
    public List<TaskGroupListVo> queryListByParentId(Integer parentId) {
        List<PointsTaskGroupEntity> list = getBaseDao().lambdaQuery()
                .eq(PointsTaskGroupEntity::getParentId, NumberUtils.isNullOrZero(parentId) ? WebCommonConstants.DATABASE_DEFAULT_INT_VALUE : parentId)
                .list();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> groupIdList = list.stream().map(PointsTaskGroupEntity::getId).toList();
        Map<Integer, Integer> groupTaskNumMap = pointsTaskBasicsDao.queryTaskNumByGroupIds(groupIdList);
        return list.stream().map(t -> {
            TaskGroupListVo vo = BeanUtil.copyProperties(t, TaskGroupListVo.class);
            vo.setTaskNum(groupTaskNumMap.get(t.getId()));
            return vo;
        }).toList();
    }

    @Override
    public StatisticsLatestTaskNumVo statisticsLatestTaskNum() {
        LocalDate nowDate = LocalDate.now();
        Long todayNum = pointsTaskBasicsDao.lambdaQuery()
                .eq(PointsTaskBasicsEntity::getDeadlineDate, nowDate)
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT.getCode())
                .count();
        Long weekNum = pointsTaskBasicsDao.lambdaQuery()
                .between(PointsTaskBasicsEntity::getDeadlineDate, nowDate, nowDate.plusDays(7L))
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT.getCode())
                .count();
        Long noGroupNum = pointsTaskBasicsDao.lambdaQuery()
                .eq(PointsTaskBasicsEntity::getTaskGroupId, WebCommonConstants.DATABASE_DEFAULT_INT_VALUE)
                .eq(PointsTaskBasicsEntity::getTaskStatus, TaskStatusEnum.WAIT.getCode())
                .count();
        return new StatisticsLatestTaskNumVo(todayNum, weekNum, noGroupNum);
    }
}
