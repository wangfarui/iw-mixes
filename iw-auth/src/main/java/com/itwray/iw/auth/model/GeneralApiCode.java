package com.itwray.iw.auth.model;

/**
 * 通用接口编码
 *
 * @author wray
 * @since 2024/3/5
 */
public enum GeneralApiCode implements ApiCode {

    CONTINUE(100, "Continue"),
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "错误请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "重定向"),
    REQUEST_TIMEOUT(408, "请求超时"),
    SERVER_ERROR(500, "服务器错误"),
    NOT_IMPLEMENTED(501, "服务未实现"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    ;

    private final int code;

    private final String message;

    GeneralApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
