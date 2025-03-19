package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务分组表 新增DTO
 *
 * @author wray
 * @since 2025-03-19
 */
@Data
@Schema(name = "任务分组表 新增DTO")
public class PointsTaskGroupAddDto implements AddDto {

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

    @Schema(title = "是否删除(true表示已删除, 默认false表示未删除)")
    private Boolean deleted;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(title = "用户id")
    private Integer userId;

}
