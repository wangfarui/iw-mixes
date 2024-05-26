package com.itwray.iw.eat.controller;

import com.itwray.iw.eat.service.EatDictService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典接口控制层
 *
 * @author wray
 * @since 2024/5/26
 */
@RestController
@RequestMapping("/dict")
public class EatDictController {

    @Resource
    private EatDictService eatDictService;

}
