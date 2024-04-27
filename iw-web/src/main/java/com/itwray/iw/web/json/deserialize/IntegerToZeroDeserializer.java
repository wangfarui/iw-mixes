package com.itwray.iw.web.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.itwray.iw.common.constants.BoolEnums;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 反序列化器, 数据为空时, 默认转换为数值0
 *
 * @author wray
 * @since 2024/4/25
 */
public class IntegerToZeroDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isBlank(p.getText())) {
            return BoolEnums.FALSE.getCode();
        }
        return p.getValueAsInt(BoolEnums.FALSE.getCode());
    }
}
