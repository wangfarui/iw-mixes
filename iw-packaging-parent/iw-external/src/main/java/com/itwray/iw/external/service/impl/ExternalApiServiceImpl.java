package com.itwray.iw.external.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.model.enums.ExternalRedisKeyEnum;
import com.itwray.iw.external.service.ExternalApiService;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.exception.IwServerException;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.utils.IpUtils;
import com.itwray.iw.web.utils.SpringWebHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外部API服务实现层
 *
 * @author wray
 * @since 2024/10/17
 */
@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    private DiscoveryClient discoveryClient;

    /**
     * 高德地图API Key
     */
    @Value("${iw.external.amap.key:}")
    private String amapKey;

    /**
     * UptimeRobot API Key
     */
    @Value("${iw.external.uptimerobot.key:}")
    private String uptimeRobotKey;

    /**
     * 每日热点API接口地址
     */
    @Value("${iw.external.dailyhot.api:}")
    private String dailyHotApi;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void heartbeat() {
        List<String> services = discoveryClient.getServices();
        for (String serviceName : ExternalClientConstants.ALL_SERVICE_NAME) {
            if (!services.contains(serviceName)) {
                throw new IwServerException(serviceName + "服务已下线");
            }
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            for (ServiceInstance instance : instances) {
                if (Boolean.FALSE.toString().equals(instance.getMetadata().get("nacos.healthy"))) {
                    throw new IwServerException(serviceName + "服务异常");
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getIpAddress() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        String clientIp = IpUtils.getClientIp(request);
        if (StringUtils.isBlank(clientIp)) {
            throw new IwWebException("无效的请求");
        }
        Map<Object, Object> ipCache = (Map<Object, Object>) RedisUtil.get(ExternalRedisKeyEnum.IP_ADDRESS_KEY.getKey(clientIp));
        if (ipCache != null) {
            return ipCache;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", this.amapKey);
        paramMap.put("ip", clientIp);
        String res = HttpUtil.get("https://restapi.amap.com/v3/ip", paramMap);
        Map<Object, Object> resMap = (Map<Object, Object>) JSONUtil.toBean(res, Map.class);
        Object adcode = resMap.get("adcode");
        if (adcode instanceof List<?> list) {
            if (list.size() == 0) {
                resMap.put("adcode", "");
            } else {
                resMap.put("adcode", list.get(0));
            }
        }
        // 缓存请求ip的地址信息
        ExternalRedisKeyEnum.IP_ADDRESS_KEY.setStringValue(resMap, clientIp);
        return resMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getWeather() {
        // 查询请求ip
        Map<Object, Object> ipAddress = getIpAddress();
        // 获取ip的城市编码
        String adcode = String.valueOf(ipAddress.get("adcode"));
        if (StringUtils.isBlank(adcode)) {
            adcode = "110000";
        }

        Map<Object, Object> resMap = this.getWeather(adcode);
        Object info = resMap.get("info");
        if (!"OK".equals(info)) {
            return getWeather("110000");
        }
        return resMap;
    }

    public Map<Object, Object> getWeather(String adcode) {
        Map<Object, Object> adcodeCache = (Map<Object, Object>) RedisUtil.get(ExternalRedisKeyEnum.CITY_WEATHER_KEY.getKey(adcode));
        if (adcodeCache != null) {
            return adcodeCache;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", this.amapKey);
        paramMap.put("city", adcode);
        String res = HttpUtil.get("https://restapi.amap.com/v3/weather/weatherInfo", paramMap);
        Map<Object, Object> resMap = (Map<Object, Object>) JSONUtil.toBean(res, Map.class);
        // 城市天气缓存3小时
        ExternalRedisKeyEnum.CITY_WEATHER_KEY.setStringValue(resMap, adcode);
        return resMap;
    }

    @Override
    public Map<Object, Object> getMonitorsByUptimeRobot(Map<String, Object> bodyParam) {
        Map<Object, Object> monitorsCache = (Map<Object, Object>) RedisUtil.get(ExternalRedisKeyEnum.SITE_MONITORS_KEY.getKey());
        if (monitorsCache != null) {
            return monitorsCache;
        }

        bodyParam.put("api_key", this.uptimeRobotKey);
        String res = HttpUtil.post("https://api.uptimerobot.com/v2/getMonitors", bodyParam);
        Map<Object, Object> resMap = (Map<Object, Object>) JSONUtil.toBean(res, Map.class);
        // 缓存个人站点的监控信息
        ExternalRedisKeyEnum.SITE_MONITORS_KEY.setStringValue(resMap);
        return resMap;
    }

    @Override
    public Map<Object, Object> getDailyHot(String source) {
        Map<Object, Object> cache = (Map<Object, Object>) RedisUtil.get(ExternalRedisKeyEnum.DAILY_HOT_KEY.getKey(source));
        if (cache != null) {
            return cache;
        }

        String res = HttpUtil.get(dailyHotApi + "/" + source + "?cache=true");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<Object, Object> resMap = objectMapper.readValue(res, Map.class);
            // 热点信息缓存30分钟
            ExternalRedisKeyEnum.DAILY_HOT_KEY.setStringValue(resMap, source);
            return resMap;
        } catch (JsonProcessingException e) {
            throw new IwWebException(e);
        }
    }
}
