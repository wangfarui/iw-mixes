package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
