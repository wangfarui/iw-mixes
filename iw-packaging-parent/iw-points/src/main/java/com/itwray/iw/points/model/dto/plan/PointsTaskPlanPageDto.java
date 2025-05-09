package com.itwray.iw.points.model.dto.plan;

import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务计划 分页DTO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointsTaskPlanPageDto extends PageDto {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 状态(0禁用 1启用)
     */
    private Integer status;
}
