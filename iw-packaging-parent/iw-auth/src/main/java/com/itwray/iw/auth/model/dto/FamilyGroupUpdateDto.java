package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家庭组 更新DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "家庭组 更新DTO")
public class FamilyGroupUpdateDto extends FamilyGroupAddDto implements UpdateDto {

    @Schema(title = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Integer id;
}
