package com.itwray.iw.external.service;

import java.util.Map;

/**
 * 外部API接口服务
 *
 * @author wray
 * @since 2024/10/17
 */
public interface ExternalApiService {

    /**
     * 获取IP地址信息
     *
     * @return IP地址信息
     */
    Map<Object, Object> getIpAddress();

    /**
     * 获取城市天气
     *
     * @return 实况天气数据信息
     */
    Map<Object, Object> getWeather();

    /**
     * 根据UptimeRobot获取站点监测情况
     *
     * @return 站点监测情况
     */
    Map<Object, Object> getMonitorsByUptimeRobot(Map<String, Object> bodyParam);
}
