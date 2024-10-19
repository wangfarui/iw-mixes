package com.itwray.iw.external.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.external.service.ExternalApiService;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.core.SpringWebHolder;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部API服务实现层
 *
 * @author wray
 * @since 2024/10/17
 */
@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    /**
     * 高德地图API Key
     */
    @Value("${iw.amap.key:}")
    private String amapKey;

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getIpAddress() {
        HttpServletRequest request = SpringWebHolder.getRequest();
        String clientIp = IpUtils.getClientIp(request);
        if (StringUtils.isBlank(clientIp)) {
            throw new IwWebException("无效的请求");
        }
        Map<Object, Object> ipCache = (Map<Object, Object>) RedisUtil.get(clientIp);
        if (ipCache != null) {
            return ipCache;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", amapKey);
        paramMap.put("ip", clientIp);
        String res = HttpUtil.get("https://restapi.amap.com/v3/ip", paramMap);
        Map<Object, Object> resMap = (Map<Object, Object>) JSONUtil.toBean(res, Map.class);
        // 请求ip缓存七天
        RedisUtil.set(clientIp, resMap, 60 * 60 * 24 * 7);
        return resMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getWeather() {
        // 查询请求ip
        Map<Object, Object> ipAddress = getIpAddress();
        // 获取ip的城市编码
        String adcode = (String) ipAddress.get("adcode");
        if (StringUtils.isBlank(adcode)) {
            throw new IwWebException("获取城市信息失败");
        }

        Map<Object, Object> adcodeCache = (Map<Object, Object>) RedisUtil.get(adcode);
        if (adcodeCache != null) {
            return adcodeCache;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", amapKey);
        paramMap.put("city", adcode);
        String res = HttpUtil.get("https://restapi.amap.com/v3/weather/weatherInfo", paramMap);
        Map<Object, Object> resMap = (Map<Object, Object>) JSONUtil.toBean(res, Map.class);
        // 城市天气缓存3小时
        RedisUtil.set(adcode, resMap, 60 * 60 * 3);
        return resMap;
    }
}
