package com.itwray.iw.bookkeeping.controller;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetUpdateDto;
import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetStatisticsVo;
import com.itwray.iw.bookkeeping.service.BookkeepingBudgetService;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 记账预算表 接口控制层
 *
 * @author wray
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/bookkeeping/budget")
@Validated
@Tag(name = "记账预算表接口")
public class BookkeepingBudgetController extends WebController<BookkeepingBudgetService,
        BookkeepingBudgetAddDto, BookkeepingBudgetUpdateDto, BookkeepingBudgetDetailVo, Integer>  {

    @Autowired
    public BookkeepingBudgetController(BookkeepingBudgetService webService) {
        super(webService);
    }

    @GetMapping("/totalBudget")
    @Operation(summary = "查询总预算")
    public BookkeepingBudgetStatisticsVo getTotalBudget(@RequestParam("budgetType") Integer budgetType) {
        BudgetTypeEnum budgetTypeEnum = ConstantEnumUtil.findByType(BudgetTypeEnum.class, budgetType);
        return getWebService().getTotalBudget(budgetTypeEnum);
    }

    @GetMapping("/categoryBudget")
    @Operation(summary = "查询分类预算")
    public List<BookkeepingBudgetStatisticsVo> getCategoryBudget(@RequestParam("budgetType") Integer budgetType) {
        BudgetTypeEnum budgetTypeEnum = ConstantEnumUtil.findByType(BudgetTypeEnum.class, budgetType);
        return getWebService().getCategoryBudget(budgetTypeEnum);
    }
}
