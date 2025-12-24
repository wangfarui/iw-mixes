package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员订阅记录表 更新DTO
 *
 * @author wray
 * @since 2025/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员订阅记录表 更新DTO")
public class BookkeepingMembershipSubscriptionUpdateDto extends BookkeepingMembershipSubscriptionAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
