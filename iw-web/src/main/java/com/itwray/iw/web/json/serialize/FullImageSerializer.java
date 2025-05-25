package com.itwray.iw.web.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.itwray.iw.common.constants.CommonConstants;
import com.itwray.iw.web.utils.EnvironmentHolder;
import com.itwray.iw.web.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 全量图片地址序列化器
 *
 * @author wray
 * @since 2024/5/10
 */
public class FullImageSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StringUtils.isBlank(value)) {
            gen.writeString(CommonConstants.EMPTY);
        } else if (FileUtils.containHttpPrefix(value)) {
            gen.writeString(value);
        } else {
            gen.writeString(EnvironmentHolder.getProperty("aliyun.oss.base-url") + value);
        }
    }

}
