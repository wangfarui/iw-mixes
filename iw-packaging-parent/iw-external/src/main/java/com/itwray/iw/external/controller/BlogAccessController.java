package com.itwray.iw.external.controller;

import com.itwray.iw.external.config.BlogAccessProperties;
import com.itwray.iw.external.model.dto.BlogAccessVerifyDto;
import com.itwray.iw.external.model.vo.BlogAccessVerifyVo;
import com.itwray.iw.external.service.BlogAccessService;
import com.itwray.iw.web.annotation.SkipWrapper;
import com.itwray.iw.web.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 静态博客文章访问控制接口。
 *
 * @author wray
 * @since 2026/6/24
 */
@SkipWrapper
@RestController
@RequestMapping("/external-service/api/blog/access")
@Validated
@Tag(name = "静态博客文章访问接口")
public class BlogAccessController {

    private static final String FAIL_MESSAGE = "访问密码不正确";

    private final BlogAccessService blogAccessService;

    private final BlogAccessProperties properties;

    public BlogAccessController(BlogAccessService blogAccessService, BlogAccessProperties properties) {
        this.blogAccessService = blogAccessService;
        this.properties = properties;
    }

    @PostMapping(value = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "校验博客文章访问密码")
    public ResponseEntity<BlogAccessVerifyVo> verify(@RequestBody(required = false) BlogAccessVerifyDto dto,
                                                     HttpServletRequest request) {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (!properties.isAllowedOrigin(origin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .headers(noStoreHeaders())
                    .body(BlogAccessVerifyVo.fail(FAIL_MESSAGE));
        }

        String clientIp = IpUtils.getClientIp(request);
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        BlogAccessVerifyVo vo = blogAccessService.verify(dto, clientIp, userAgent);
        HttpStatus status = vo.isOk() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status)
                .headers(accessHeaders(origin))
                .body(vo);
    }

    @RequestMapping(value = "/verify", method = RequestMethod.OPTIONS)
    @Operation(summary = "博客文章访问密码校验预检请求")
    public ResponseEntity<Void> options(HttpServletRequest request) {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (!properties.isAllowedOrigin(origin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .headers(noStoreHeaders())
                    .build();
        }
        return ResponseEntity.noContent()
                .headers(accessHeaders(origin))
                .build();
    }

    private HttpHeaders accessHeaders(String origin) {
        HttpHeaders headers = noStoreHeaders();
        if (StringUtils.isNotBlank(origin)) {
            headers.setAccessControlAllowOrigin(origin);
            headers.add(HttpHeaders.VARY, HttpHeaders.ORIGIN);
            headers.setAccessControlAllowMethods(
                    java.util.List.of(org.springframework.http.HttpMethod.POST, org.springframework.http.HttpMethod.OPTIONS));
            headers.setAccessControlAllowHeaders(java.util.List.of(HttpHeaders.CONTENT_TYPE));
            headers.setAccessControlMaxAge(3600L);
        }
        return headers;
    }

    private HttpHeaders noStoreHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noStore());
        headers.setPragma("no-cache");
        headers.setExpires(0L);
        return headers;
    }
}
