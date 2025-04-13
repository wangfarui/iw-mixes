package com.itwray.iw.web.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwray.iw.common.constants.CommonConstants;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 反序列化器, 集合转逗号拼接
 *
 * @author wray
 * @since 2024/4/25
 */
public class CollectionToCommaSpliceDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        Collection<Integer> list = mapper.readValue(p, new TypeReference<>() {
        });

        if (list == null || list.isEmpty()) {
            return CommonConstants.EMPTY;
        }

        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}