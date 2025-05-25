package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetStatisticsVo;
import com.itwray.iw.web.service.WebService;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingBudgetUpdateDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingBudgetDetailVo;

import java.util.List;

/**
 * 记账预算表 服务接口
 *
 * @author wray
 * @since 2025-04-24
 */
public interface BookkeepingBudgetService extends WebService<BookkeepingBudgetAddDto, BookkeepingBudgetUpdateDto, BookkeepingBudgetDetailVo, Integer> {

    BookkeepingBudgetStatisticsVo getTotalBudget(BudgetTypeEnum budgetType);

    List<BookkeepingBudgetStatisticsVo> getCategoryBudget(BudgetTypeEnum budgetType);
}
