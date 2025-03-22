package com.itwray.iw.points.model.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 固定分组任务数量统计VO
 *
 * @author wray
 * @since 2025/3/21
 */
@Data
@Schema(name = "固定分组任务数量统计VO")
public class FixedGroupTaskNumVo {

    @Schema(title = "今日任务数量")
    private Integer todayNum;

    @Schema(title = "最近7天任务数量")
    private Integer last7DayNum;

    @Schema(title = "默认分组(收集箱)任务数量")
    private Integer defaultGroupNum;
}
