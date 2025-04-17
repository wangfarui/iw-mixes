package com.itwray.iw.points.dao;

import cn.hutool.core.collection.CollUtil;
import com.itwray.iw.points.mapper.PointsTaskGroupMapper;
import com.itwray.iw.points.model.entity.PointsTaskGroupEntity;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务分组表 DAO
 *
 * @author wray
 * @since 2025-03-19
 */
@Component
public class PointsTaskGroupDao extends BaseDao<PointsTaskGroupMapper, PointsTaskGroupEntity> {

    public Map<Integer, String> queryTaskGroupNameMap(List<Integer> taskGroupIdList) {
        Map<Integer, String> gourpNameMap = new HashMap<>();
        gourpNameMap.put(0, "收集箱");

        List<PointsTaskGroupEntity> groupEntityList = this.lambdaQuery().in(PointsTaskGroupEntity::getId, taskGroupIdList)
                .select(PointsTaskGroupEntity::getId, PointsTaskGroupEntity::getParentId, PointsTaskGroupEntity::getGroupName)
                .list();

        List<Integer> parentIdList = groupEntityList.stream().map(PointsTaskGroupEntity::getParentId)
                .filter(parentId -> !WebCommonConstants.DATABASE_DEFAULT_INT_VALUE.equals(parentId))
                .toList();
        if (CollUtil.isEmpty(parentIdList)) {
            gourpNameMap.putAll(groupEntityList.stream().collect(Collectors.toMap(PointsTaskGroupEntity::getId, PointsTaskGroupEntity::getGroupName)));
            return gourpNameMap;
        }

        Map<Integer, String> parentGroupNameMap = this.lambdaQuery().in(PointsTaskGroupEntity::getId, parentIdList)
                .select(PointsTaskGroupEntity::getId, PointsTaskGroupEntity::getGroupName)
                .list()
                .stream()
                .collect(Collectors.toMap(PointsTaskGroupEntity::getId, PointsTaskGroupEntity::getGroupName));

        for (PointsTaskGroupEntity groupEntity : groupEntityList) {
            String parentName = parentGroupNameMap.get(groupEntity.getParentId());
            if (parentName != null) {
                gourpNameMap.put(groupEntity.getId(), parentName + "-" + groupEntity.getGroupName());
            } else {
                gourpNameMap.put(groupEntity.getId(), groupEntity.getGroupName());
            }
        }

        return gourpNameMap;
    }
}
