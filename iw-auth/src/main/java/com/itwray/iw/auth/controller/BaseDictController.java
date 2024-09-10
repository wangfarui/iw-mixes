package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.vo.BaseDictVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;
import com.itwray.iw.auth.service.BaseDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典接口控制层
 *
 * @author wray
 * @since 2024/5/26
 */
@RestController
@RequestMapping("/dict")
@Tag(name = "字典接口")
public class BaseDictController {

    @Resource
    private BaseDictService baseDictService;


    @GetMapping("/getDictTypeList")
    @Operation(summary = "查询字典类型集合")
    public List<DictTypeVo> getDictTypeList() {
        return baseDictService.getDictTypeList();
    }

    @GetMapping("/getDictListByType")
    @Operation(summary = "查询字典类型集合")
    public List<BaseDictVo> getDictListByType(@RequestParam("dictType") Integer dictType) {
        return baseDictService.getDictList(dictType);
    }
}
