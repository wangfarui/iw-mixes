package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 应用账号信息 新增DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
@Schema(name = "应用账号信息 新增DTO")
public class ApplicationAccountAddDto implements AddDto {

    @Schema(title = "应用分类")
    private Integer type;

    @Schema(title = "应用名称")
    @Length(max = 32, message = "应用名称不能超过32字符")
    private String name;

    @Schema(title = "应用地址")
    @Length(max = 255, message = "应用地址不能超过255字符")
    private String address;

    @Schema(title = "账号")
    @Length(max = 64, message = "账号不能超过64字符")
    private String account;

    @Schema(title = "密码")
    @Length(max = 64, message = "密码不能超过64字符")
    private String password;

    @Schema(title = "备注")
    @Length(max = 255, message = "备注不能超过255字符")
    private String remark;
}
