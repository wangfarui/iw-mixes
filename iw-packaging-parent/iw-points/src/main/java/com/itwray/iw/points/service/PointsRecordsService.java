package com.itwray.iw.points.service;

import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.PointsRecordsPageDto;
import com.itwray.iw.points.model.dto.PointsRecordsStatisticsDto;
import com.itwray.iw.points.model.dto.PointsRecordsUpdateDto;
import com.itwray.iw.points.model.vo.PointsRecordsDetailVo;
import com.itwray.iw.points.model.vo.PointsRecordsPageVo;
import com.itwray.iw.points.model.vo.PointsRecordsStatisticsVo;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.WebService;

/**
 * 积分记录 接口服务
 *
 * @author wray
 * @since 2024/9/26
 */
public interface PointsRecordsService extends WebService<PointsRecordsAddDto, PointsRecordsUpdateDto, PointsRecordsDetailVo> {

    /**
     * 分页查询积分记录
     *
     * @param dto 分页查询对象
     * @return 积分记录分页响应对象
     */
    PageVo<PointsRecordsPageVo> page(PointsRecordsPageDto dto);

    /**
     * 统计积分记录
     *
     * @param dto 统计查询对象
     * @return 统计信息
     */
    PointsRecordsStatisticsVo statistics(PointsRecordsStatisticsDto dto);
}
