package com.itwray.iw.external.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.external.dao.ExternalExchangeRateDao;
import com.itwray.iw.external.model.dto.GetExchangeRateDto;
import com.itwray.iw.external.model.entity.ExternalExchangeRateEntity;
import com.itwray.iw.external.model.vo.GetExchangeRateVo;
import com.itwray.iw.external.service.InternalApiService;
import com.itwray.iw.web.constants.WebCommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部接口服务实现层
 *
 * @author wray
 * @since 2025/4/12
 */
@Service
@Slf4j
public class InternalApiServiceImpl implements InternalApiService {

    @Autowired
    private ExternalExchangeRateDao externalExchangeRateDao;

    /**
     * 汇率api的accessKey
     */
    @Value("${iw.external.exchangeRate.key:}")
    private String exchangeRateAccessKey;

    @Override
    public GetExchangeRateVo getExchangeRate(GetExchangeRateDto dto) {
        if (dto.getQueryDate() == null || dto.getQueryDate().isAfter(LocalDate.now())) {
            dto.setQueryDate(LocalDate.now());
        }
        if (dto.getFromAmount() == null) {
            dto.setFromAmount(BigDecimal.ONE);
        }

        // 查询历史汇率
        ExternalExchangeRateEntity oldExchangeRateEntity = externalExchangeRateDao.lambdaQuery()
                .eq(ExternalExchangeRateEntity::getQueryDate, dto.getQueryDate())
                .eq(ExternalExchangeRateEntity::getFromCurrency, dto.getFromCurrency())
                .eq(ExternalExchangeRateEntity::getToCurrency, dto.getToCurrency())
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (oldExchangeRateEntity != null) {
            BigDecimal toAmount = oldExchangeRateEntity.getExchangeRate().multiply(dto.getFromAmount());
            GetExchangeRateVo vo = BeanUtil.copyProperties(dto, GetExchangeRateVo.class);
            vo.setToAmount(toAmount);
            vo.setExchangeRate(oldExchangeRateEntity.getExchangeRate());
            return vo;
        }

        // 在线查询汇率
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("from", dto.getFromCurrency());
        paramMap.put("to", dto.getToCurrency());
        paramMap.put("date", dto.getQueryDate().format(DateUtils.DATE_FORMATTER));
        paramMap.put("access_key", exchangeRateAccessKey);
        paramMap.put("amount", dto.getFromAmount());
        String res = HttpUtil.get("http://api.exchangerate.host/convert", paramMap);
        log.info("InternalApiService#getExchangeRate paramMap: {}, res: {}", JSONUtil.toJsonStr(paramMap), res);
        JSONObject jsonObject = JSONUtil.parseObj(res);

        // 保存查询后的汇率
        ExternalExchangeRateEntity entity = new ExternalExchangeRateEntity();
        entity.setFromCurrency(dto.getFromCurrency());
        entity.setToCurrency(dto.getToCurrency());
        entity.setExchangeRate(jsonObject.getJSONObject("info").getBigDecimal("quote"));
        entity.setQueryDate(dto.getQueryDate());
        entity.setFromAmount(dto.getFromAmount());
        entity.setToAmount(jsonObject.getBigDecimal("result"));
        externalExchangeRateDao.save(entity);

        return BeanUtil.copyProperties(entity, GetExchangeRateVo.class);
    }
}
