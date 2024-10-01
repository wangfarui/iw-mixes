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
import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.dto.UpdateDto;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 积分记录 服务实现层
 *
 * @author wray
 * @since 2024/9/26
 */
@Service
public class PointsRecordsServiceImpl extends WebServiceImpl<PointsRecordsMapper, PointsRecordsEntity,
        PointsRecordsDao, PointsRecordsDetailVo> implements PointsRecordsService {

    private final PointsTotalDao pointsTotalDao;

    @Autowired
    public PointsRecordsServiceImpl(PointsRecordsDao baseDao, PointsTotalDao pointsTotalDao) {
        super(baseDao);
        this.pointsTotalDao = pointsTotalDao;
    }

    @Override
    @Transactional
    public Serializable add(AddDto dto) {
        if (dto instanceof PointsRecordsAddDto recordsAddDto) {
            recordsAddDto.setTransactionType(recordsAddDto.getPoints() < 0 ? PointsTransactionTypeEnum.DEDUCT.getCode() : PointsTransactionTypeEnum.INCREASE.getCode());
            pointsTotalDao.updatePointsBalance(recordsAddDto.getPoints());
        }
        return super.add(dto);
    }

    @Override
    @Transactional
    public void update(UpdateDto dto) {
        // 查询记录实体
        PointsRecordsEntity pointsRecordsEntity = getBaseDao().queryById(dto.getId());

        // 同步积分余额
        if (dto instanceof PointsRecordsUpdateDto recordsUpdateDto) {
            recordsUpdateDto.setTransactionType(recordsUpdateDto.getPoints() < 0 ? PointsTransactionTypeEnum.DEDUCT.getCode() : PointsTransactionTypeEnum.INCREASE.getCode());
            // 把之前的记录扣减，再增加
            pointsTotalDao.updatePointsBalance(recordsUpdateDto.getPoints() - pointsRecordsEntity.getPoints());
        }

        // 更新记录
        PointsRecordsEntity entity = BeanUtil.copyProperties(dto, PointsRecordsEntity.class);
        getBaseDao().updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Serializable id) {
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
}
