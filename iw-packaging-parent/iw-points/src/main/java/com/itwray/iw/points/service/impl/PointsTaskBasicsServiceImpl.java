package com.itwray.iw.points.service.impl;

import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.mapper.PointsTaskBasicsMapper;
import com.itwray.iw.points.model.dto.PointsTaskBasicsAddDto;
import com.itwray.iw.points.model.dto.PointsTaskBasicsUpdateDto;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.vo.PointsTaskBasicsDetailVo;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 任务基础表 服务实现类
 *
 * @author wray
 * @since 2025-03-19
 */
@Service
public class PointsTaskBasicsServiceImpl extends WebServiceImpl<PointsTaskBasicsMapper, PointsTaskBasicsEntity, PointsTaskBasicsDao,
        PointsTaskBasicsAddDto, PointsTaskBasicsUpdateDto, PointsTaskBasicsDetailVo, Integer> implements PointsTaskBasicsService {

    public PointsTaskBasicsServiceImpl(PointsTaskBasicsDao baseDao) {
        super(baseDao);
    }
}
