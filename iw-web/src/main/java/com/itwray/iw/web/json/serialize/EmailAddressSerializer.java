package com.itwray.iw.web.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 邮箱地址序列化器
 *
 * @author farui.wang
 * @since 2025/6/30
 */
public class EmailAddressSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StringUtils.isBlank(value) || value.length() < 8) {
            return;
        }
        gen.writeString(value.substring(0, 3) + "****" + value.substring(7));
    }
}
