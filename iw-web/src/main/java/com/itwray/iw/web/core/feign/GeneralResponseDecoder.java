package com.itwray.iw.web.core.feign;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.web.exception.FeignClientException;
import feign.Response;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通用响应对象的Feign解码器
 *
 * @author wray
 * @since 2024/9/29
 */
public class GeneralResponseDecoder implements Decoder {

    private final Decoder defaultDecoder;

    public GeneralResponseDecoder(Decoder defaultDecoder) {
        this.defaultDecoder = defaultDecoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignClientException {
        // 使用默认的解码器获取GeneralResponse响应体
        GeneralResponse<?> generalResponse = (GeneralResponse<?>) defaultDecoder.decode(response, GeneralResponse.class);

        // 判断调用是否成功
        if (!generalResponse.isSuccess()) {
            // 如果失败，抛出自定义异常
            throw new FeignClientException(generalResponse.getCode(), generalResponse.getMessage());
        }

        // 如果FeignClient方法出参为GeneralResponse, 则直接返回GeneralResponse对象
        if (type instanceof ParameterizedType parameterizedType) {
            if (parameterizedType.getRawType() instanceof Class<?> clazz) {
                if (GeneralResponse.class.isAssignableFrom(clazz)) {
                    return generalResponse;
                }
            }
        }

        // 如果成功，返回data字段的值
        return generalResponse.getData();
    }

}
