package com.itwray.iw.web.json.deserialize;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日期开始时间的反序列化器
 * <p>数据有值时，时分秒默认为 00:00:00 </p>
 *
 * @author wray
 * @since 2024/9/30
 */
public class StartLocalDateTimeDeserializer extends AbstractLocalDateDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime doDeserialize(LocalDate localDate) {
        return localDate.atTime(0, 0, 0);
    }
}
