package com.itwray.iw.points.model.dto.task;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务分组表 新增DTO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@Schema(name = "任务分组表 新增DTO")
public class TaskGroupAddDto implements AddDto {

    @Schema(title = "父分组id")
    private Integer parentId;

    @Schema(title = "分组名称")
    private String groupName;

    @Schema(title = "是否置顶任务 0-否 1-是")
    private Integer isTop;

    @Schema(title = "排序 0-默认排序")
    private Integer sort;
}
