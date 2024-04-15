package com.itwray.iw.common;

import lombok.Data;

/**
 * 通用响应对象
 *
 * @author wray
 * @since 2024/3/5
 */
@Data
public class GeneralResponse<T> {

    /**
     * 响应编码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public GeneralResponse() {
        this(GeneralApiCode.SUCCESS);
    }

    public GeneralResponse(ApiCode apiCode) {
        this.code = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public GeneralResponse(T data) {
        this();
        this.data = data;
    }

    public GeneralResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GeneralResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> GeneralResponse<T> success() {
        return new GeneralResponse<>();
    }

    public static <T> GeneralResponse<T> success(T data) {
        return new GeneralResponse<>(data);
    }

    public static <T> GeneralResponse<T> fail() {
        return new GeneralResponse<>(GeneralApiCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> GeneralResponse<T> fail(String message) {
        return new GeneralResponse<>(GeneralApiCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }
}
