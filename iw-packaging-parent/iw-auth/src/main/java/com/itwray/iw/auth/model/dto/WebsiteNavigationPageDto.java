package com.itwray.iw.auth.model.dto;

import com.itwray.iw.auth.model.enums.WebsiteNavigationStatusEnum;
import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站导航记录分页 DTO
 *
 * @author wray
 * @since 2026/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "网站导航记录分页DTO")
public class WebsiteNavigationPageDto extends PageDto {

    @Schema(title = "网站名称")
    private String name;

    @Schema(title = "网站分类")
    private String category;

    @Schema(title = "标签关键字")
    private String tag;

    @Schema(title = "网站状态(1在线 2离线)")
    private WebsiteNavigationStatusEnum status;

    @Schema(title = "是否共享(0不共享 1共享)")
    private Integer shared;
}
