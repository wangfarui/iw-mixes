package com.itwray.iw.points.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.EndLocalDateTimeDeserializer;
import com.itwray.iw.web.json.deserialize.StartLocalDateTimeDeserializer;
import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 积分记录 分页DTO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointsRecordsPageDto extends PageDto {

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

    /**
     * 积分来源
     */
    private String source;
}
