package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站导航记录 更新DTO
 *
 * @author wray
 * @since 2026/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "网站导航记录 更新DTO")
public class WebsiteNavigationUpdateDto extends WebsiteNavigationAddDto implements UpdateDto {

    @Schema(title = "id")
    @NotNull(message = "id不能为空")
    private Integer id;
}
