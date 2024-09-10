package com.itwray.iw.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典类型 VO
 *
 * @author wray
 * @since 2024/9/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "字典类型VO")
public class DictTypeVo {

    @Schema(title = "字典类型编码")
    private Integer code;

    @Schema(title = "字典类型名称")
    private String name;
}
