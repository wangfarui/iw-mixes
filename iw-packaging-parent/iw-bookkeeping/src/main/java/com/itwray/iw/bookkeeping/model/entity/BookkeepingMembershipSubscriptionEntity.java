package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.bookkeeping.model.enums.MembershipBillingCycleEnum;
import com.itwray.iw.bookkeeping.model.enums.MembershipCycleUnitEnum;
import com.itwray.iw.bookkeeping.utils.MembershipBillingCycleUtils;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 会员订阅记录表
 *
 * @author wray
 * @since 2025/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_membership_subscription")
public class BookkeepingMembershipSubscriptionEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 会员类型
     *
     * @see com.itwray.iw.web.model.enums.DictTypeEnum#BOOKKEEPING_MEMBERSHIP_TYPE
     */
    private Integer membershipType;

    /**
     * 会员名称
     */
    private String membershipName;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 计费周期
     * @see MembershipBillingCycleUtils
     */
    private MembershipBillingCycleEnum billingCycle;

    /**
     * 自定义周期间隔数
     */
    private Integer cycleNum;

    /**
     * 自定义周期单位
     */
    private MembershipCycleUnitEnum cycleUnit;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 是否自动续费(true表示开启自动续费, 默认false表示未开启)
     */
    private Boolean autoRenew;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 提前提醒天数
     */
    private Integer remindDays;

    /**
     * 备注
     */
    private String remark;
}
