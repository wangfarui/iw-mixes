package com.itwray.iw.points.model.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务分组列表
 *
 * @author wray
 * @since 2025/3/19
 */
@Data
@Schema(name = "任务分组表 详情VO")
public class TaskGroupListVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "父分组id")
    private Integer parentId;

    @Schema(title = "分组名称")
    private String groupName;

    @Schema(title = "是否置顶任务 0-否 1-是")
    private Integer isTop;

    @Schema(title = "排序 0-默认排序")
    private Integer sort;

    @Schema(title = "任务数量")
    private Integer taskNum;
}
