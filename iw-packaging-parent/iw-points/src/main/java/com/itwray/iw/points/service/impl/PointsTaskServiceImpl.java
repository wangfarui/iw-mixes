package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.points.dao.PointsRecordsDao;
import com.itwray.iw.points.dao.PointsTaskDao;
import com.itwray.iw.points.dao.PointsTotalDao;
import com.itwray.iw.points.mapper.PointsTaskMapper;
import com.itwray.iw.points.model.dto.PointsTaskAddDto;
import com.itwray.iw.points.model.dto.PointsTaskUpdateDto;
import com.itwray.iw.points.model.entity.PointsRecordsEntity;
import com.itwray.iw.points.model.entity.PointsTaskEntity;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.points.model.vo.PointsTaskDetailVo;
import com.itwray.iw.points.model.vo.PointsTaskListVo;
import com.itwray.iw.points.service.PointsTaskService;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分记录 服务实现层
 *
 * @author wray
 * @since 2024/9/26
 */
@Service
public class PointsTaskServiceImpl extends WebServiceImpl<PointsTaskMapper, PointsTaskEntity, PointsTaskDao,
        PointsTaskAddDto, PointsTaskUpdateDto, PointsTaskDetailVo> implements PointsTaskService {

    private final PointsTotalDao pointsTotalDao;

    private final PointsRecordsDao pointsRecordsDao;

    @Autowired
    public PointsTaskServiceImpl(PointsTaskDao baseDao, PointsTotalDao pointsTotalDao, PointsRecordsDao pointsRecordsDao) {
        super(baseDao);
        this.pointsTotalDao = pointsTotalDao;
        this.pointsRecordsDao = pointsRecordsDao;
    }

    @Override
    @Transactional
    public Integer add(PointsTaskAddDto dto) {

        return null;
    }

    @Override
    @Transactional
    public void update(PointsTaskUpdateDto dto) {
        super.update(dto);
    }

    @Override
    @Transactional
    public void delete(Serializable id) {
        super.delete(id);
    }

    @Override
    public PointsTaskDetailVo detail(Serializable id) {
        return super.detail(id);
    }

    @Override
    @Transactional
    public void submit(Integer id) {
        // 查询积分任务
        PointsTaskEntity pointsTaskEntity = getBaseDao().queryById(id);

        // 先更新总积分
        pointsTotalDao.updatePointsBalance(pointsTaskEntity.getTaskPoints());

        // 再插入一条新积分记录
        PointsRecordsEntity pointsRecordsEntity = new PointsRecordsEntity();
        pointsRecordsEntity.setTransactionType(PointsTransactionTypeEnum.getCodeByPoints(pointsTaskEntity.getTaskPoints()));
        pointsRecordsEntity.setPoints(pointsTaskEntity.getTaskPoints());
        pointsRecordsEntity.setSource(pointsTaskEntity.getTaskName());
        pointsRecordsEntity.setSourceType(PointsSourceTypeEnum.POINTS_TASK_MANUAL.getCode());
        pointsRecordsDao.save(pointsRecordsEntity);
    }

    /**
     * 查询当前用户的任务列表
     *
     * @return 任务列表
     */
    @Override
    public List<PointsTaskListVo> list() {
        return getBaseDao().list().stream().map(t -> BeanUtil.copyProperties(t, PointsTaskListVo.class)).collect(Collectors.toList());
    }
}
