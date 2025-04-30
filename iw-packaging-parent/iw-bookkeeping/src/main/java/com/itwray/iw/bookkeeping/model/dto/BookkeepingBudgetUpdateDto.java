package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账预算表 更新DTO
 *
 * @author wray
 * @since 2025-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "记账预算表 更新DTO")
public class BookkeepingBudgetUpdateDto extends BookkeepingBudgetAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
