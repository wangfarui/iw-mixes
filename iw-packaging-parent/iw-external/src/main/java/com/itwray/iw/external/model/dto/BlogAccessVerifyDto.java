package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 博客文章访问密码校验 DTO。
 *
 * @author wray
 * @since 2026/6/24
 */
@Data
@Schema(name = "博客文章访问密码校验DTO")
public class BlogAccessVerifyDto {

    @Schema(title = "文章ID")
    private String postId;

    @Schema(title = "文章路径")
    private String path;

    @Schema(title = "访问授权范围")
    private String scope;

    @Schema(title = "用户输入的访问密码")
    private String password;
}
