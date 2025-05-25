package com.itwray.iw.eat.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.eat.model.dto.MealAddDto;
import com.itwray.iw.eat.model.dto.MealPageDto;
import com.itwray.iw.eat.model.dto.MealUpdateDto;
import com.itwray.iw.eat.model.vo.MealDetailVo;
import com.itwray.iw.eat.model.vo.MealDishesMaterialDetailVo;
import com.itwray.iw.eat.model.vo.MealPageVo;
import com.itwray.iw.eat.service.EatMealService;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用餐表 前端控制器
 *
 * @author wray
 * @since 2024-04-23
 */
@RestController
@RequestMapping("/eat/meal")
@Validated
@Tag(name = "用餐接口")
public class EatMealController {

    @Resource
    private EatMealService eatMealService;

    @PostMapping("/add")
    @Operation(summary = "新增用餐信息")
    public GeneralResponse<Integer> add(@RequestBody @Valid MealAddDto dto) {
        return GeneralResponse.success(eatMealService.add(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "修改用餐信息")
    public void update(@RequestBody @Valid MealUpdateDto dto) {
        eatMealService.update(dto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用餐信息")
    public void delete(@RequestParam("id") Integer id) {
        eatMealService.delete(id);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询用餐列表")
    public PageVo<MealPageVo> page(@RequestBody @Valid MealPageDto dto) {
        return eatMealService.page(dto);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询用餐详情")
    public MealDetailVo detail(@RequestParam("id") Integer id) {
        return eatMealService.detail(id);
    }

    @GetMapping("/dishes/materialDetail")
    @Operation(summary = "获取用餐记录的菜品食材详情信息")
    public MealDishesMaterialDetailVo dishesMaterialDetail(@RequestParam("id") Integer mealId) {
        return eatMealService.dishesMaterialDetail(mealId);
    }
}
