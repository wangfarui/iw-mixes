package com.itwray.iw.points.model.vo;

import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 任务关联表 详情VO
 *
 * @author wray
 * @since 2025-04-17
 */
@Data
@Schema(name = "任务关联表 详情VO")
public class PointsTaskRelationDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "任务id")
    private Integer taskId;

    @Schema(title = "奖励积分")
    private Integer rewardPoints;

    @Schema(title = "处罚积分")
    private Integer punishPoints;

}
