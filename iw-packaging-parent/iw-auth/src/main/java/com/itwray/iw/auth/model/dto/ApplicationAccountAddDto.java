package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 应用账号信息 新增DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
public class ApplicationAccountAddDto implements AddDto {

    @Schema(title = "应用名称")
    private String name;

    @Schema(title = "应用地址")
    private String address;

    @Schema(title = "账号")
    private String account;

    @Schema(title = "密码")
    private String password;

    @Schema(title = "备注")
    private String remark;
}
