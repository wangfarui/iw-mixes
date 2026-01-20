package com.itwray.iw.eat.controller;

import com.itwray.iw.eat.model.dto.EatFridgeFoodAddDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodPageDto;
import com.itwray.iw.eat.model.dto.EatFridgeFoodUpdateDto;
import com.itwray.iw.eat.model.vo.EatFridgeFoodDetailVo;
import com.itwray.iw.eat.model.vo.EatFridgeFoodPageVo;
import com.itwray.iw.eat.service.EatFridgeFoodService;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 冰箱食材表 接口控制层
 *
 * @author wray
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/fridge/food")
@Validated
@Tag(name = "冰箱食材表接口")
public class EatFridgeFoodController extends WebController<EatFridgeFoodService,
        EatFridgeFoodAddDto, EatFridgeFoodUpdateDto, EatFridgeFoodDetailVo, Integer>  {

    @Autowired
    public EatFridgeFoodController(EatFridgeFoodService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询冰箱食材列表")
    public PageVo<EatFridgeFoodPageVo> page(@RequestBody @Valid EatFridgeFoodPageDto dto) {
        return getWebService().page(dto);
    }

    @PutMapping("/markAsUsed")
    @Operation(summary = "标记食材已用完")
    public void markAsUsed(@RequestParam("id") Integer id) {
        getWebService().markAsUsed(id);
    }

}
