package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息 修改DTO
 *
 * @author wray
 * @since 2025/6/30
 */
@Data
@Schema(name = "用户信息 修改DTO")
public class UserInfoEditDto {

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "头像（url地址）")
    private String avatar;
}
