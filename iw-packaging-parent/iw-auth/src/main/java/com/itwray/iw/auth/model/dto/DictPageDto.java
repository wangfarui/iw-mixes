package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典信息分页 DTO
 *
 * @author wray
 * @since 2024/9/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "字典信息分页DTO")
public class DictPageDto extends PageDto {

    @Schema(title = "字典类型")
    private Integer dictType;

    @Schema(title = "字典code")
    private Integer dictCode;

    @Schema(title = "字典名称")
    private String dictName;

    @Schema(title = "字典状态(0禁用 1启用)")
    private Integer dictStatus;
}
