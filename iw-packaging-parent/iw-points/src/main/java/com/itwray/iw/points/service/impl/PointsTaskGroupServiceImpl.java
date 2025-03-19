package com.itwray.iw.points.service.impl;

import com.itwray.iw.points.dao.PointsTaskGroupDao;
import com.itwray.iw.points.mapper.PointsTaskGroupMapper;
import com.itwray.iw.points.model.dto.PointsTaskGroupAddDto;
import com.itwray.iw.points.model.dto.PointsTaskGroupUpdateDto;
import com.itwray.iw.points.model.entity.PointsTaskGroupEntity;
import com.itwray.iw.points.model.vo.PointsTaskGroupDetailVo;
import com.itwray.iw.points.service.PointsTaskGroupService;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 任务分组表 服务实现类
 *
 * @author wray
 * @since 2025-03-19
 */
@Service
public class PointsTaskGroupServiceImpl extends WebServiceImpl<PointsTaskGroupMapper, PointsTaskGroupEntity, PointsTaskGroupDao,
        PointsTaskGroupAddDto, PointsTaskGroupUpdateDto, PointsTaskGroupDetailVo, Integer> implements PointsTaskGroupService {

    public PointsTaskGroupServiceImpl(PointsTaskGroupDao baseDao) {
        super(baseDao);
    }
}
