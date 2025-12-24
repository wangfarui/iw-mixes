package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.MembershipBillingCycleEnum;
import com.itwray.iw.bookkeeping.model.enums.MembershipCycleUnitEnum;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 会员订阅记录表 新增DTO
 *
 * @author wray
 * @since 2025/11/5
 */
@Data
@Schema(name = "会员订阅记录表 新增DTO")
public class BookkeepingMembershipSubscriptionAddDto implements AddDto {
    
    @Schema(title = "会员类型")
    @NotNull(message = "会员类型不能为空")
    private Integer membershipType;

    @Schema(title = "会员名称")
    @NotBlank(message = "会员名称不能为空")
    private String membershipName;

    @Schema(title = "金额")
    @Min(value = 0, message = "金额不能小于0")
    @Max(value = 999999, message = "金额不能大于999999")
    private BigDecimal amount;

    @Schema(title = "计费周期")
    @NotNull(message = "计费周期不能为空")
    private MembershipBillingCycleEnum billingCycle;

    @Schema(title = "自定义周期间隔数")
    private Integer cycleNum;

    @Schema(title = "自定义周期单位")
    private MembershipCycleUnitEnum cycleUnit;

    @Schema(title = "开始日期")
    @NotNull(message = "开始日期不能为空")
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

}
