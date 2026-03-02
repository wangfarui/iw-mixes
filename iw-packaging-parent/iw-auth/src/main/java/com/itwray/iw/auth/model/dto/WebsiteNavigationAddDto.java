package com.itwray.iw.auth.model.dto;

import com.itwray.iw.auth.model.enums.WebsiteNavigationStatusEnum;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 网站导航记录 新增DTO
 *
 * @author wray
 * @since 2026/2/28
 */
@Data
@Schema(name = "网站导航记录 新增DTO")
public class WebsiteNavigationAddDto implements AddDto {

    @Schema(title = "网站名称")
    @NotBlank(message = "网站名称不能为空")
    @Length(max = 64, message = "网站名称不能超过64字符")
    private String name;

    @Schema(title = "网站链接")
    @NotBlank(message = "网站链接不能为空")
    @Length(max = 255, message = "网站链接不能超过255字符")
    private String url;

    @Schema(title = "网站描述")
    @Length(max = 255, message = "网站描述不能超过255字符")
    private String description;

    @Schema(title = "网站图标URL")
    @Length(max = 255, message = "网站图标URL不能超过255字符")
    private String icon;

    @Schema(title = "网站分类")
    @Length(max = 32, message = "网站分类不能超过32字符")
    private String category;

    @Schema(title = "标签列表")
    private List<String> tags;

    @Schema(title = "网站状态(1在线 2离线)")
    private WebsiteNavigationStatusEnum status;

    @Schema(title = "是否共享(0不共享 1共享)")
    private Integer shared;
}
