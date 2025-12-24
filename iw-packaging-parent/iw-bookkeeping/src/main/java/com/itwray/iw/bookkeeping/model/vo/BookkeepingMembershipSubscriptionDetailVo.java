package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.MembershipBillingCycleEnum;
import com.itwray.iw.bookkeeping.model.enums.MembershipCycleUnitEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员订阅记录表 详情VO
 *
 * @author wray
 * @since 2025/11/5
 */
@Data
@Schema(name = "会员订阅记录表 详情VO")
public class BookkeepingMembershipSubscriptionDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "会员类型")
    private Integer membershipType;

    @Schema(title = "会员名称")
    private String membershipName;

    @Schema(title = "金额")
    private BigDecimal amount;

    @Schema(title = "计费周期")
    private MembershipBillingCycleEnum billingCycle;

    @Schema(title = "自定义周期间隔数")
    private Integer cycleNum;

    @Schema(title = "自定义周期单位")
    private MembershipCycleUnitEnum cycleUnit;

    @Schema(title = "开始日期")
    private LocalDate startDate;

    @Schema(title = "结束日期")
    private LocalDate endDate;

    @Schema(title = "是否自动续费(true表示开启自动续费, 默认false表示未开启)")
    private Boolean autoRenew;

    @Schema(title = "支付方式")
    private String payWay;

    @Schema(title = "提前提醒天数")
    private Integer remindDays;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;

}
