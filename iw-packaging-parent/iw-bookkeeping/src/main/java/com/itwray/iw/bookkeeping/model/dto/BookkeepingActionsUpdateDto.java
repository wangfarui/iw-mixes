package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账行为表 更新DTO
 *
 * @author wray
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "记账行为表 更新DTO")
public class BookkeepingActionsUpdateDto extends BookkeepingActionsAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
