package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import lombok.Data;

/**
 * 积分任务 新增DTO
 *
 * @author wray
 * @since 2024/10/9
 */
@Data
public class PointsTaskAddDto implements AddDto {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务积分分数(可以是正数或负数)
     */
    private Integer taskPoints;
}
