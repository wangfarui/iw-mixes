package com.itwray.iw.web.utils;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.web.exception.IwWebException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 {@link HttpUtil} 工具类, 简化 HTTP 请求工具
 *
 * @author wangfarui
 * @since 2022/9/28
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class HttpUtils {

    /**
     * 发送post请求
     *
     * @param url     请求完整路径
     * @param body    body请求对象
     * @param header  请求头header
     * @param resType 响应对象类型
     * @param <T>     body请求对象类型
     * @param <R>     响应对象类型
     * @return 响应对象实体
     */
    public static <T, R> R post(String url, T body, Map<String, String> header, Object resType) {
        HttpWrapRequest<?> request = createRequest(url).setBody(body).setHeader(header);
        return post(request, resType);
    }

    /**
     * 发送post请求
     *
     * @param request 请求对象
     * @param resType 响应类型
     * @param <T>     请求对象body类型
     * @param <R>     响应对象类型
     * @return 响应对象实体
     */
    public static <T, R> R post(HttpWrapRequest<T> request, Object resType) {
        String url = request.url;
        T body = request.getBody();
        Map<String, String> header = request.getHeader();

        HttpRequest httpRequest = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(body), ContentType.JSON.getValue());
        if (request.getCharset() != null) {
            httpRequest.charset(request.getCharset());
        }
        if (!header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpRequest.header(entry.getKey(), entry.getValue());
            }
        }
        try(HttpResponse httpResponse = httpRequest.execute()) {
            String resStr = httpResponse.body();
            if (resType instanceof Class) {
                Class<R> typeClazz = (Class<R>) resType;
                return JSONUtil.toBean(resStr, typeClazz);
            } else {
                return (R) resStr;
            }
        } catch (HttpException e) {
            log.error(String.format("HTTP远程调用请求异常, url: %s, req: %s, header: %s", url, JSONUtil.toJsonStr(body), header), e);
            throw IwWebException.withoutDingTalk("远程调用请求异常");
        } catch (Throwable e) {
            log.error(String.format("HTTP调用请求异常, url: %s, req: %s, header: %s", url, JSONUtil.toJsonStr(body), header), e);
            throw IwWebException.withoutDingTalk("调用请求异常");
        }
    }

    public static <T> HttpWrapRequest<T> createRequest(String url) {
        return createRequest(Method.GET, url);
    }

    public static <T> HttpWrapRequest<T> createRequest(Method method, String url) {
        return new HttpWrapRequest<>(method, url);
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class HttpWrapRequest<T> {

        /**
         * TODO 暂时无用. 默认只执行post请求
         */
        private Method method;

        private String url;

        private T body;

        private Map<String, String> header = new HashMap<>();

        /**
         * 字符编码默认为 UTF-8
         */
        private Charset charset;

        public HttpWrapRequest() {
        }

        public HttpWrapRequest(Method method, String url) {
            this.method = method;
            this.url = url;
        }

        /**
         * 配置授权token
         *
         * @param authorization 授权token
         * @return HttpWrapRequest
         */
        public HttpWrapRequest<T> setAuthorization(String authorization) {
            this.header.put(Header.AUTHORIZATION.getValue(), authorization);
            return this;
        }

        /**
         * 增加请求头参数 (默认覆盖重复key)
         *
         * @param key   请求头key
         * @param value 请求头value
         * @return HttpWrapRequest
         */
        public HttpWrapRequest<T> addHeaderParam(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        /**
         * 使用 POST 方式调用, 返回指定响应对象类型
         *
         * @param resClazz 响应对象类型
         * @param <R>      响应对象类型
         * @return 响应对象
         */
        public <R> R executePost(Class<R> resClazz) {
            return post(this.url, this.body, this.header, resClazz);
        }

        /**
         * 使用 POST 方式调用
         *
         * @return 字符串形式的响应结果
         */
        public String executePost() {
            return executePost(String.class);
        }
    }
}
