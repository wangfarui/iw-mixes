package com.itwray.iw.points.model.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务分组表 详情VO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@Schema(name = "任务分组表 详情VO")
public class TaskGroupDetailVo implements DetailVo {

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

    @Schema(title = "创建时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime updateTime;

}
