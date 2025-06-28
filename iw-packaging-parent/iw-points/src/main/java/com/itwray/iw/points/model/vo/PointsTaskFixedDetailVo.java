package com.itwray.iw.points.model.vo;

import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 常用任务表 详情VO
 *
 * @author wray
 * @since 2025-06-06
 */
@Data
@Schema(name = "常用任务表 详情VO")
public class PointsTaskFixedDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "任务名称")
    private String taskName;

    @Schema(title = "任务积分(可以是正数或负数)")
    private Integer taskPoints;

    @Schema(title = "任务备注")
    private String taskRemark;

}
