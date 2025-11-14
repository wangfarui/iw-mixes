package com.itwray.iw.bookkeeping.model.enums;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingMembershipSubscriptionEntity;
import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import com.itwray.iw.web.model.enums.SortTypeEnum;
import lombok.Getter;

/**
 * 会员订阅排序类型枚举
 *
 * @author farui.wang
 * @since 2025/5/6
 */
@Getter
public enum MembershipSubscriptionSortTypeEnum implements BusinessConstantEnum, SortTypeEnum<BookkeepingMembershipSubscriptionEntity> {

    DEFAULT(0, "id", "默认"),
    AMOUNT(1, "amount", "金额"),
    END_DATE(2, "end_date", "结束时间"),
    START_DATE(3, "start_date", "开始时间");

    private final Integer code;

    private final String name;

    private final String desc;

    MembershipSubscriptionSortTypeEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public SFunction<BookkeepingMembershipSubscriptionEntity, ?> getSortField() {
        if (this == DEFAULT) {
            return BookkeepingMembershipSubscriptionEntity::getId;
        } else if (this == AMOUNT) {
            return BookkeepingMembershipSubscriptionEntity::getAmount;
        } else if (this == END_DATE) {
            return BookkeepingMembershipSubscriptionEntity::getEndDate;
        } else if (this == START_DATE) {
            return BookkeepingMembershipSubscriptionEntity::getStartDate;
        }
        return BookkeepingMembershipSubscriptionEntity::getId;
    }
}
