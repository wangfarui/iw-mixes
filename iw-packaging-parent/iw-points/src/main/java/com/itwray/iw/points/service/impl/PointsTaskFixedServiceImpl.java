package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.points.service.PointsTaskFixedService;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.model.enums.mq.PointsRecordsTopicEnum;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.points.model.dto.PointsTaskFixedAddDto;
import com.itwray.iw.points.model.dto.PointsTaskFixedUpdateDto;
import com.itwray.iw.points.model.vo.PointsTaskFixedDetailVo;
import com.itwray.iw.points.dao.PointsTaskFixedDao;
import com.itwray.iw.points.mapper.PointsTaskFixedMapper;
import com.itwray.iw.points.model.entity.PointsTaskFixedEntity;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 常用任务表 服务实现类
 *
 * @author wray
 * @since 2025-06-06
 */
@Service
public class PointsTaskFixedServiceImpl extends WebServiceImpl<PointsTaskFixedDao, PointsTaskFixedMapper, PointsTaskFixedEntity,
        PointsTaskFixedAddDto, PointsTaskFixedUpdateDto, PointsTaskFixedDetailVo, Integer> implements PointsTaskFixedService {

    @Autowired
    public PointsTaskFixedServiceImpl(PointsTaskFixedDao baseDao) {
        super(baseDao);
    }

    @Override
    public void submit(Integer id) {
        PointsTaskFixedEntity taskFixedEntity = getBaseDao().queryById(id);
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setPoints(taskFixedEntity.getTaskPoints());
        pointsRecordsAddDto.setSource(taskFixedEntity.getTaskName());
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.FIXED_TASK.getCode());
        pointsRecordsAddDto.setRemark(taskFixedEntity.getTaskRemark());
        pointsRecordsAddDto.setUserId(taskFixedEntity.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.TASK_FIXED, pointsRecordsAddDto);
    }

    @Override
    public List<PointsTaskFixedDetailVo> list() {
        List<PointsTaskFixedEntity> fixedEntityList = getBaseDao().lambdaQuery()
                .eq(PointsTaskFixedEntity::getUserId, UserUtils.getUserId())
                .orderByAsc(PointsTaskFixedEntity::getId)
                .list();
        return fixedEntityList.stream().map(t -> BeanUtil.copyProperties(t, PointsTaskFixedDetailVo.class)).toList();
    }
}
