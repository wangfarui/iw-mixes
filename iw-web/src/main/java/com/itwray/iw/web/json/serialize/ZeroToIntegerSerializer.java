package com.itwray.iw.web.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.constants.CommonConstants;

import java.io.IOException;

/**
 * 序列化器, 数据为0时, 默认转换为空字符串
 *
 * @author wray
 * @since 2024/4/25
 */
public class ZeroToIntegerSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (BoolEnum.TRUE.getCode().equals(value)) {
            gen.writeString(CommonConstants.EMPTY);
        } else {
            gen.writeObject(value);
        }
    }
}
