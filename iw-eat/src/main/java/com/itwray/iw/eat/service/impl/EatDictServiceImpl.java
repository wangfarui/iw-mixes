package com.itwray.iw.eat.service.impl;

import com.itwray.iw.eat.dao.EatDictDao;
import com.itwray.iw.eat.service.EatDictService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 字典服务实现层
 *
 * @author wray
 * @since 2024/5/26
 */
@Service
public class EatDictServiceImpl implements EatDictService {

    @Resource
    private EatDictDao eatDictDao;

}
