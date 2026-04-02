package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI任务更新DTO
 *
 * @author wray
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "AI任务更新DTO")
public class AiTaskUpdateDto extends AiTaskAddDto implements UpdateDto {

    @Schema(title = "id")
    @NotNull(message = "id不能为空")
    private Integer id;
}
