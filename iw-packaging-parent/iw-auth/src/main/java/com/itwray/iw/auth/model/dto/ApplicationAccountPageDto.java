package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账号信息分页 DTO
 *
 * @author wray
 * @since 2024/9/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "应用账号信息分页DTO")
public class ApplicationAccountPageDto extends PageDto {

    @Schema(title = "应用分类")
    private Integer type;

    @Schema(title = "应用名称")
    private String name;

    @Schema(title = "应用地址")
    private String address;

}
