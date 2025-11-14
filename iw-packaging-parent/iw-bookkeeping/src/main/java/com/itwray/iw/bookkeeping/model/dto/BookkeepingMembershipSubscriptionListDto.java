package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.MembershipSubscriptionExpiryTypeEnum;
import com.itwray.iw.bookkeeping.model.enums.MembershipSubscriptionSortTypeEnum;
import com.itwray.iw.web.model.enums.SortWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员订阅记录 列表查询DTO
 *
 * @author wray
 * @since 2025/11/12
 */
@Data
public class BookkeepingMembershipSubscriptionListDto {

    @Schema(title = "会员类型")
    private Integer membershipType;

    /**
     * 记账记录排序类型
     */
    private MembershipSubscriptionSortTypeEnum sortType;

    /**
     * 排序方式 1=升序, 其他值=降序
     */
    private SortWayEnum sortWay;

    /**
     * 到期类型
     */
    private MembershipSubscriptionExpiryTypeEnum expiryType;
}
