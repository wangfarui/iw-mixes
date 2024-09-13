package com.itwray.iw.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典列表精简信息 VO
 *
 * @author wray
 * @since 2024/9/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "字典列表精简信息VO")
public class DictAllListVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "字典code")
    private Integer dictCode;

    @Schema(title = "字典名称")
    private String dictName;
}
