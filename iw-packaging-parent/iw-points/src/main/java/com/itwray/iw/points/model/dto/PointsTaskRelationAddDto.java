package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 任务关联表 新增DTO
 *
 * @author wray
 * @since 2025-04-17
 */
@Data
@Schema(name = "任务关联表 新增DTO")
public class PointsTaskRelationAddDto implements AddDto {

    @Schema(title = "任务id")
    @NotNull(message = "任务id不能为空")
    private Integer taskId;

    @Schema(title = "奖励积分")
    private Integer rewardPoints;

    @Schema(title = "处罚积分")
    private Integer punishPoints;

}
