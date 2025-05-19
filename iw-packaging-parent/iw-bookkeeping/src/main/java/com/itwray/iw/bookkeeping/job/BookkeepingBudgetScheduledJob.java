package com.itwray.iw.bookkeeping.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.bookkeeping.dao.BookkeepingBudgetDao;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingBudgetEntity;
import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 积分任务的定时任务
 *
 * @author farui.wang
 * @since 2025/5/12
 */
@Component
@Slf4j
public class BookkeepingBudgetScheduledJob {

    private final BookkeepingBudgetDao bookkeepingBudgetDao;

    public BookkeepingBudgetScheduledJob(BookkeepingBudgetDao bookkeepingBudgetDao) {
        this.bookkeepingBudgetDao = bookkeepingBudgetDao;
    }

    /**
     * 每月1号凌晨0点执行, 生成新的月度预算数据
     * TODO 前提条件：bookkeeping服务是单实例
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void handleMonthBudgetData() {
        LocalDate nowMonth = DateUtils.startDateOfNowMonth();
        List<BookkeepingBudgetEntity> monthBudgetList;
        try {
            UserUtils.setUserDataPermission(false);
            // 查询上个月所有的月度预算数据
            monthBudgetList = bookkeepingBudgetDao.lambdaQuery()
                    .in(BookkeepingBudgetEntity::getBudgetType, BudgetTypeEnum.MONTH, BudgetTypeEnum.MONTH_CATEGORY)
                    .eq(BookkeepingBudgetEntity::getBudgetMonth, nowMonth.minusMonths(1))
                    .list();
        } finally {
            UserUtils.removeUserDataPermission();
        }
        if (CollUtil.isNotEmpty(monthBudgetList)) {
            monthBudgetList.forEach(t -> {
                this.initDefaultFieldValue(t);
                t.setBudgetMonth(nowMonth);
            });
            System.out.println("aaaList monthBudgetList");
            System.out.println(JSONUtil.toJsonStr(monthBudgetList));
            bookkeepingBudgetDao.saveBatch(monthBudgetList);
        }
    }

    /**
     * 每年1月1号凌晨0点执行, 生成新的年度预算数据
     * TODO 前提条件：bookkeeping服务是单实例
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void handleYearBudgetData() {
        int nowYear = LocalDate.now().getYear();
        List<BookkeepingBudgetEntity> yearBudgetList;
        try {
            UserUtils.setUserDataPermission(false);
            // 查询去年所有的年度预算数据
            yearBudgetList = bookkeepingBudgetDao.lambdaQuery()
                    .in(BookkeepingBudgetEntity::getBudgetType, BudgetTypeEnum.YEAR, BudgetTypeEnum.YEAR_CATEGORY)
                    .eq(BookkeepingBudgetEntity::getBudgetYear, nowYear - 1)
                    .list();
        } finally {
            UserUtils.removeUserDataPermission();
        }
        if (CollUtil.isNotEmpty(yearBudgetList)) {
            yearBudgetList.forEach(t -> {
                this.initDefaultFieldValue(t);
                t.setBudgetYear(nowYear);
            });
            System.out.println("aaaList yearBudgetList");
            System.out.println(JSONUtil.toJsonStr(yearBudgetList));
            bookkeepingBudgetDao.saveBatch(yearBudgetList);
        }
    }

    private void initDefaultFieldValue(BookkeepingBudgetEntity entity) {
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);
    }
}
