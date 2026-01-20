package com.itwray.iw.eat.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 冰箱食材表 新增DTO
 *
 * @author wray
 * @since 2026-01-20
 */
@Data
@Schema(name = "冰箱食材表 新增DTO")
public class EatFridgeFoodAddDto implements AddDto {

    @Schema(title = "食材名称")
    @NotBlank(message = "食材名称不能为空")
    private String name;

    @Schema(title = "食材图标")
    private String emoji;

    @Schema(title = "食材分类")
    private Integer category;

    @Schema(title = "食材分区")
    private Integer section;

    @Schema(title = "数量")
    private String quantity;

    @Schema(title = "入库日期")
    private LocalDate addDate;

    @Schema(title = "过期日期")
    private LocalDate expireDate;
}
