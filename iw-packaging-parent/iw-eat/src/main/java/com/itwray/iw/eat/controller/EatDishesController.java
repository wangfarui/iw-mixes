package com.itwray.iw.eat.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.eat.model.dto.DishesAddDto;
import com.itwray.iw.eat.model.dto.DishesPageDto;
import com.itwray.iw.eat.model.dto.DishesUpdateDto;
import com.itwray.iw.eat.model.vo.DishesDetailVo;
import com.itwray.iw.eat.model.vo.DishesPageVo;
import com.itwray.iw.eat.service.EatDishesService;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品表 前端控制器
 *
 * @author wray
 * @since 2024-04-23
 */
@RestController
@RequestMapping("/dishes")
@Validated
@Tag(name = "菜品接口")
public class EatDishesController {

    @Resource
    private EatDishesService eatDishesService;

    @PostMapping("/add")
    @Operation(summary = "新增菜品信息")
    public GeneralResponse<Integer> add(@RequestBody @Valid DishesAddDto dto) {
        return GeneralResponse.success(eatDishesService.add(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "修改菜品信息")
    public void update(@RequestBody @Valid DishesUpdateDto dto) {
        eatDishesService.update(dto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除菜品信息")
    public void delete(@RequestParam("id") Integer id) {
        eatDishesService.delete(id);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询菜品列表")
    public PageVo<DishesPageVo> page(@RequestBody @Valid DishesPageDto dto) {
        return eatDishesService.page(dto);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询菜品详情")
    public DishesDetailVo detail(@RequestParam("id") Integer id) {
        return eatDishesService.detail(id);
    }

    @GetMapping("/recommendDishes")
    @Operation(summary = "获取推荐菜品")
    public List<DishesPageVo> recommendDishes() {
        return eatDishesService.recommendDishes();
    }
}
