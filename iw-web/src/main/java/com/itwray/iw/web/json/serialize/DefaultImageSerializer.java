package com.itwray.iw.web.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.itwray.iw.web.utils.EnvironmentHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 默认图片序列化器
 *
 * @author wray
 * @since 2024/5/10
 */
public class DefaultImageSerializer  extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StringUtils.isBlank(value)) {
            gen.writeString(EnvironmentHolder.getProperty("iw.default.image-url", "https://itwray.oss-cn-heyuan.aliyuncs.com/img/20240510170053.png"));
        } else {
            gen.writeString(value);
        }
    }
}
