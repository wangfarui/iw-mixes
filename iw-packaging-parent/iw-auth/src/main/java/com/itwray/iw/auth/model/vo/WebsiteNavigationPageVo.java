package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.auth.model.enums.WebsiteNavigationStatusEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.PageRecordVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 网站导航记录分页 VO
 *
 * @author wray
 * @since 2026/2/28
 */
@Data
@Schema(name = "网站导航记录分页VO")
public class WebsiteNavigationPageVo implements PageRecordVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "网站名称")
    private String name;

    @Schema(title = "网站链接")
    private String url;

    @Schema(title = "网站描述")
    private String description;

    @Schema(title = "网站图标URL")
    private String icon;

    @Schema(title = "网站分类")
    private String category;

    @Schema(title = "标签列表")
    private List<String> tags;

    @Schema(title = "网站状态(1在线 2离线)")
    private WebsiteNavigationStatusEnum status;

    @Schema(title = "是否共享(0不共享 1共享)")
    private Integer shared;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime updateTime;
}
