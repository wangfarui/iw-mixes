package com.itwray.iw.web.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.itwray.iw.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * 抽象LocalDate反序列化器
 * <p>数据为空时，返回null</p>
 *
 * @author wray
 * @since 2024/9/30
 */
public abstract class AbstractLocalDateDeserializer<T extends Temporal> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        if (StringUtils.isBlank(date)) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT));
        return doDeserialize(localDate);
    }

    public abstract T doDeserialize(LocalDate localDate);
}
