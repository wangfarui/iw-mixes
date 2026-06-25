package com.itwray.iw.external.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 博客文章访问密码校验 VO。
 *
 * @author wray
 * @since 2026/6/24
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "博客文章访问密码校验VO")
public class BlogAccessVerifyVo {

    @Schema(title = "是否校验成功")
    private boolean ok;

    @Schema(title = "访问授权类型")
    private String access;

    @Schema(title = "访问授权范围")
    private String scope;

    @Schema(title = "文章ID")
    private String postId;

    @Schema(title = "短期访问令牌")
    private String token;

    @Schema(title = "令牌过期时间")
    private String expiresAt;

    @Schema(title = "base64url编码的32字节AES key")
    private String key;

    @Schema(title = "失败消息")
    private String message;

    public static BlogAccessVerifyVo success(String scope, String postId, String token, String expiresAt, String key) {
        BlogAccessVerifyVo vo = new BlogAccessVerifyVo();
        vo.setOk(true);
        vo.setAccess("scope");
        vo.setScope(scope);
        vo.setPostId(postId);
        vo.setToken(token);
        vo.setExpiresAt(expiresAt);
        vo.setKey(key);
        return vo;
    }

    public static BlogAccessVerifyVo fail(String message) {
        BlogAccessVerifyVo vo = new BlogAccessVerifyVo();
        vo.setOk(false);
        vo.setMessage(message);
        return vo;
    }
}
