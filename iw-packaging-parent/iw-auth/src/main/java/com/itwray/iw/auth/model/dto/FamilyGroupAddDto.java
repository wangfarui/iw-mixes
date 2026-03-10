package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 家庭组 新增DTO
 *
 * @author wray
 * @since 2024-03-10
 */
@Data
@Schema(name = "家庭组 新增DTO")
public class FamilyGroupAddDto implements AddDto {

    @Schema(title = "家庭组名称")
    @NotBlank(message = "家庭组名称不能为空")
    @Length(max = 32, message = "家庭组名称不能超过32字符")
    private String groupName;

    @Schema(title = "家庭组头像")
    @Length(max = 255, message = "家庭组头像不能超过255字符")
    private String groupAvatar;

    @Schema(title = "家庭组描述")
    @Length(max = 255, message = "家庭组描述不能超过255字符")
    private String groupDesc;

    @Schema(title = "最大成员数")
    @Min(value = 2, message = "最大成员数不能小于2")
    @Max(value = 100, message = "最大成员数不能超过100")
    private Integer maxMember = 10;
}
