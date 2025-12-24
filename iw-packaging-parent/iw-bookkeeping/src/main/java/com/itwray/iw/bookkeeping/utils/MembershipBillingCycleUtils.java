package com.itwray.iw.bookkeeping.utils;

import com.itwray.iw.bookkeeping.model.entity.BookkeepingMembershipSubscriptionEntity;
import com.itwray.iw.bookkeeping.model.enums.MembershipBillingCycleEnum;
import com.itwray.iw.bookkeeping.model.enums.MembershipCycleUnitEnum;

import java.time.LocalDate;

/**
 * 会员记账周期工具类
 *
 * @author wray
 * @since 2025/11/12
 */
public abstract class MembershipBillingCycleUtils {

    public static LocalDate computeBillingCycleDate(BookkeepingMembershipSubscriptionEntity membershipSubscriptionEntity) {
        return MembershipBillingCycleUtils.computeBillingCycleDate(
                membershipSubscriptionEntity.getStartDate(),
                membershipSubscriptionEntity.getBillingCycle(),
                membershipSubscriptionEntity.getCycleNum(),
                membershipSubscriptionEntity.getCycleUnit()
        );
    }

    /**
     * 计算会员计费周期结束日期
     *
     * @param startDate    开始日期
     * @param billingCycle 计费周期
     * @param cycleNum     自定义周期间隔数
     * @param cycleUnit    自定义周期单位
     * @return 结束日期
     */
    public static LocalDate computeBillingCycleDate(LocalDate startDate,
                                                    MembershipBillingCycleEnum billingCycle,
                                                    Integer cycleNum,
                                                    MembershipCycleUnitEnum cycleUnit) {
        if (startDate == null) {
            return null;
        }
        switch (billingCycle) {
            case MONTHLY -> {
                return startDate.plusMonths(1);
            }
            case YEARLY -> {
                return startDate.plusYears(1);
            }
            case WEEKLY -> {
                return startDate.plusWeeks(1);
            }
            case DAILY -> {
                return startDate.plusDays(1);
            }
            case CUSTOM -> {
                if (cycleNum == null || cycleUnit == null) {
                    return null;
                }
                switch (cycleUnit) {
                    case DAY -> {
                        return startDate.plusDays(cycleNum);
                    }
                    case WEEK -> {
                        return startDate.plusWeeks(cycleNum);
                    }
                    case MONTH -> {
                        return startDate.plusMonths(cycleNum);
                    }
                    case YEAR -> {
                        return startDate.plusYears(cycleNum);
                    }
                    default -> {
                        return null;
                    }
                }
            }
            default -> {
                return null;
            }
        }
    }
}
