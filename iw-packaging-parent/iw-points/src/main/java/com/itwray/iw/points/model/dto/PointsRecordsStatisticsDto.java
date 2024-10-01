package com.itwray.iw.points.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.EndLocalDateTimeDeserializer;
import com.itwray.iw.web.json.deserialize.StartLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录 统计DTO
 *
 * @author wray
 * @since 2024/9/30
 */
@Data
public class PointsRecordsStatisticsDto {

    /**
     * 积分变动类型(1表示增加, 2表示扣减)
     */
    private Integer transactionType;

    /**
     * 积分记录开始时间
     */
    @JsonDeserialize(using = StartLocalDateTimeDeserializer.class)
    private LocalDateTime createStartTime;

    /**
     * 积分记录结束时间
     */
    @JsonDeserialize(using = EndLocalDateTimeDeserializer.class)
    private LocalDateTime createEndTime;
}
