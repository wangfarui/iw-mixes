package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.DictAddDto;
import com.itwray.iw.auth.model.dto.DictPageDto;
import com.itwray.iw.auth.model.dto.DictUpdateDto;
import com.itwray.iw.auth.model.vo.BaseDictVo;
import com.itwray.iw.auth.model.vo.DictDetailVo;
import com.itwray.iw.auth.model.vo.DictPageVo;
import com.itwray.iw.auth.model.vo.DictTypeVo;
import com.itwray.iw.auth.service.BaseDictService;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典接口控制层
 *
 * @author wray
 * @since 2024/5/26
 */
@RestController
@RequestMapping("/dict")
@Validated
@Tag(name = "字典接口")
public class BaseDictController extends WebController<BaseDictService, DictAddDto, DictUpdateDto, DictDetailVo> {

    @Autowired
    public BaseDictController(BaseDictService baseDictService) {
        super(baseDictService);
    }

    @GetMapping("/getDictTypeList")
    @Operation(summary = "查询字典类型集合")
    public List<DictTypeVo> getDictTypeList() {
        return getWebService().getDictTypeList();
    }

    @GetMapping("/getDictListByType")
    @Operation(summary = "查询字典列表")
    public List<BaseDictVo> getDictListByType(@RequestParam("dictType") Integer dictType) {
        return getWebService().getDictList(dictType);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询字典")
    public PageVo<DictPageVo> page(@RequestBody @Valid DictPageDto dto) {
        return getWebService().page(dto);
    }
}
