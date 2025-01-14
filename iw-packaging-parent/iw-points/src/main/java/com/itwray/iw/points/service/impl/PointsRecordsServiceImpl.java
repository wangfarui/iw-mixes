package com.itwray.iw.points.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.points.dao.PointsRecordsDao;
import com.itwray.iw.points.dao.PointsTotalDao;
import com.itwray.iw.points.mapper.PointsRecordsMapper;
import com.itwray.iw.points.model.bo.PointsRecordsStatisticsBo;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.PointsRecordsPageDto;
import com.itwray.iw.points.model.dto.PointsRecordsStatisticsDto;
import com.itwray.iw.points.model.dto.PointsRecordsUpdateDto;
import com.itwray.iw.points.model.entity.PointsRecordsEntity;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.points.model.vo.PointsRecordsDetailVo;
import com.itwray.iw.points.model.vo.PointsRecordsPageVo;
import com.itwray.iw.points.model.vo.PointsRecordsStatisticsVo;
import com.itwray.iw.points.service.PointsRecordsService;
import com.itwray.iw.starter.rocketmq.config.RocketMQClientListener;
import com.itwray.iw.web.constants.MQTopicConstants;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 积分记录 服务实现层
 *
 * @author wray
 * @since 2024/9/26
 */
@Service
@RocketMQMessageListener(consumerGroup = "points-records-service", topic = MQTopicConstants.POINTS_RECORDS, tag = "*")
public class PointsRecordsServiceImpl extends WebServiceImpl<PointsRecordsMapper, PointsRecordsEntity, PointsRecordsDao,
        PointsRecordsAddDto, PointsRecordsUpdateDto, PointsRecordsDetailVo, Integer>
        implements PointsRecordsService, RocketMQClientListener<PointsRecordsAddDto> {

    private final PointsTotalDao pointsTotalDao;

    @Autowired
    public PointsRecordsServiceImpl(PointsRecordsDao baseDao, PointsTotalDao pointsTotalDao) {
        super(baseDao);
        this.pointsTotalDao = pointsTotalDao;
    }

    @Override
    @Transactional
    public Integer add(PointsRecordsAddDto dto) {
        dto.setTransactionType(PointsTransactionTypeEnum.getCodeByPoints(dto.getPoints()));
        pointsTotalDao.updatePointsBalance(dto.getPoints());
        return super.add(dto);
    }

    @Override
    @Transactional
    public void update(PointsRecordsUpdateDto dto) {
        // 查询记录实体
        PointsRecordsEntity pointsRecordsEntity = getBaseDao().queryById(dto.getId());

        // 同步积分余额
        dto.setTransactionType(PointsTransactionTypeEnum.getCodeByPoints(dto.getPoints()));
        // 把之前的记录扣减，再增加
        pointsTotalDao.updatePointsBalance(dto.getPoints() - pointsRecordsEntity.getPoints());

        // 更新记录
        PointsRecordsEntity entity = BeanUtil.copyProperties(dto, PointsRecordsEntity.class);
        getBaseDao().updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 查询记录实体
        PointsRecordsEntity pointsRecordsEntity = getBaseDao().queryById(id);

        // 把之前的记录扣减
        pointsTotalDao.updatePointsBalance(-pointsRecordsEntity.getPoints());

        super.delete(id);
    }

    @Override
    public PageVo<PointsRecordsPageVo> page(PointsRecordsPageDto dto) {
        LambdaQueryWrapper<PointsRecordsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getTransactionType() != null, PointsRecordsEntity::getTransactionType, dto.getTransactionType())
                .between(dto.getCreateStartTime() != null && dto.getCreateEndTime() != null,
                        PointsRecordsEntity::getCreateTime, dto.getCreateStartTime(), dto.getCreateEndTime())
                .orderByDesc(PointsRecordsEntity::getId);
        return getBaseDao().page(dto, queryWrapper, PointsRecordsPageVo.class);
    }

    @Override
    public PointsRecordsStatisticsVo statistics(PointsRecordsStatisticsDto dto) {
        if (dto.getCreateStartTime() == null) {
            dto.setCreateStartTime(DateUtils.startTimeOfNowMonth());
        }
        if (dto.getCreateEndTime() == null) {
            dto.setCreateEndTime(DateUtils.endTimeOfNowMonth());
        }

        // 查询积分变动类型对应的积分变动数量
        Map<Integer, Integer> pointsMap = getBaseDao().getBaseMapper().statistics(dto)
                .stream()
                .collect(Collectors.toMap(PointsRecordsStatisticsBo::getTransactionType, PointsRecordsStatisticsBo::getTotalPoints));

        PointsRecordsStatisticsVo statisticsVo = new PointsRecordsStatisticsVo();
        statisticsVo.setIncreasePoints(pointsMap.getOrDefault(PointsTransactionTypeEnum.INCREASE.getCode(), 0));
        statisticsVo.setDeductPoints(pointsMap.getOrDefault(PointsTransactionTypeEnum.DEDUCT.getCode(), 0));
        return statisticsVo;
    }


    @Override
    public Class<PointsRecordsAddDto> getGenericClass() {
        return PointsRecordsAddDto.class;
    }

    @Override
    public void doConsume(PointsRecordsAddDto recordsAddDto) {
        this.add(recordsAddDto);
    }
}
