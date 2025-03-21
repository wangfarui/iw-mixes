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
import java.util.stream.Collectors;

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

        // 如果parentId为空,则表示当前查询的是清单分组,需要统计所有清单分组下子分组的所有任务数量
        if (NumberUtils.isNullOrZero(parentId)) {
            List<PointsTaskGroupEntity> subGroupList = getBaseDao().lambdaQuery()
                    .in(PointsTaskGroupEntity::getParentId, groupIdList)
                    .select(PointsTaskGroupEntity::getId, PointsTaskGroupEntity::getParentId)
                    .list();

            Map<Integer, List<Integer>> parentIdMap = subGroupList.stream()
                    .collect(Collectors.groupingBy(PointsTaskGroupEntity::getParentId,
                            Collectors.mapping(PointsTaskGroupEntity::getId, Collectors.toList())
                            ));

            groupIdList = subGroupList.stream().map(PointsTaskGroupEntity::getId).toList();
            Map<Integer, Integer> groupTaskNumMap = pointsTaskBasicsDao.queryTaskNumByGroupIds(groupIdList);
            return list.stream().map(t -> {
                TaskGroupListVo vo = BeanUtil.copyProperties(t, TaskGroupListVo.class);
                List<Integer> subGroupIdList = parentIdMap.get(t.getId());
                if (subGroupIdList == null) {
                    vo.setTaskNum(0);
                    return vo;
                }
                int taskNum = 0;
                for (Integer subGroupId : subGroupIdList) {
                    taskNum += groupTaskNumMap.getOrDefault(subGroupId, 0);
                }
                vo.setTaskNum(taskNum);
                return vo;
            }).toList();
        } else {
            Map<Integer, Integer> groupTaskNumMap = pointsTaskBasicsDao.queryTaskNumByGroupIds(groupIdList);
            return list.stream().map(t -> {
                TaskGroupListVo vo = BeanUtil.copyProperties(t, TaskGroupListVo.class);
                vo.setTaskNum(groupTaskNumMap.get(t.getId()));
                return vo;
            }).toList();
        }
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
