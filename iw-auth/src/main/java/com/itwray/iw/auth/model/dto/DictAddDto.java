package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典信息 新增DTO
 *
 * @author wray
 * @since 2024/9/11
 */
@Data
@Schema(name = "字典信息 新增DTO")
public class DictAddDto implements AddDto {

    @Schema(title = "父字典id")
    private Integer parentId;

    @Schema(title = "字典类型")
    private Integer dictType;

    @Schema(title = "字典code")
    private Integer dictCode;

    @Schema(title = "字典名称")
    private String dictName;

    @Schema(title = "排序")
    private Integer sort;
}
