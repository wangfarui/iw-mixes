package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典信息 编辑DTO
 *
 * @author wray
 * @since 2024/9/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "字典信息 编辑DTO")
public class DictUpdateDto extends DictAddDto implements UpdateDto {

    @Schema(title = "id")
    private Integer id;
}
