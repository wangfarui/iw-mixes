package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分任务 更新DTO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointsTaskUpdateDto extends PointsTaskAddDto implements UpdateDto {

    private Integer id;
}
