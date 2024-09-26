package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分记录 更新DTO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointsRecordsUpdateDto extends PointsRecordsAddDto implements UpdateDto {

    private Integer id;
}
