package com.itwray.iw.eat.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 冰箱食材表 更新DTO
 *
 * @author wray
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "冰箱食材表 更新DTO")
public class EatFridgeFoodUpdateDto extends EatFridgeFoodAddDto implements UpdateDto {

    @NotNull(message = "id不能为空")
    @Schema(title = "id")
    private Integer id;
}
