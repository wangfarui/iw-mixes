package com.itwray.iw.points.model.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 任务分组可移动列表VO
 *
 * @author wray
 * @since 2025/3/28
 */
@Data
@Schema(name = "任务分组可移动列表VO")
public class TaskGroupMoveListVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "清单名称")
    private String groupName;

    @Schema(title = "清单下的任务分组列表")
    private List<TaskGroupMoveListVo> subGroupList;

    public TaskGroupMoveListVo() {
    }

    public TaskGroupMoveListVo(Integer id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }
}
