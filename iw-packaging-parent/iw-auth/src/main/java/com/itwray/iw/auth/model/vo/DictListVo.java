package com.itwray.iw.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典列表信息VO VO
 *
 * @author wray
 * @since 2024/9/10
 */
@Data
@Schema(name = "字典列表信息VO")
public class DictListVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "父字典id")
    private Integer parentId;

    @Schema(title = "字典类型")
    private Integer dictType;

    @Schema(title = "字典code")
    private Integer dictCode;

    @Schema(title = "字典名称")
    private String dictName;

    @Schema(title = "字典状态(0禁用 1启用)")
    private Integer dictStatus;

    @Schema(title = "排序")
    private Integer sort;
}
