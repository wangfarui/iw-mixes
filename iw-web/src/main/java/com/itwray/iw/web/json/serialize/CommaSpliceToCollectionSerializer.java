package com.itwray.iw.web.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 序列化器, 逗号拼接转集合
 *
 * @author wray
 * @since 2024/4/25
 */
public class CommaSpliceToCollectionSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value == null || value.trim().isEmpty()) {
            gen.writeStartArray();
            gen.writeEndArray();
            return;
        }

        String[] parts = value.split(",");
        gen.writeStartArray();
        for (String part : parts) {
            try {
                gen.writeNumber(Integer.parseInt(part.trim()));
            } catch (NumberFormatException e) {
                gen.writeNull();
            }
        }
        gen.writeEndArray();
    }
}
