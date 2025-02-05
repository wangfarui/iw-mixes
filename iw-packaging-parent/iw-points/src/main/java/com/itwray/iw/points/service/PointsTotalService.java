package com.itwray.iw.points.service;

/**
 * 积分合计表 服务接口层
 *
 * @author wray
 * @since 2024/9/26
 */
public interface PointsTotalService {

    /**
     * 获取当前用户积分余额
     *
     * @return 积分余额
     */
    Integer getPointsBalance();
}
