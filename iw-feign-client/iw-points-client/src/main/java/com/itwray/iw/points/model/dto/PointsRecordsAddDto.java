package com.itwray.iw.points.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 积分记录 新增DTO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
public class PointsRecordsAddDto implements AddDto {

    /**
     * 积分变动类型(1表示增加, 2表示扣减)
     */
    private Integer transactionType;

    /**
     * 积分变动数量(可以是正数或负数)
     */
    @NotNull(message = "积分变动数量不能为空")
    private Integer points;

    /**
     * 积分来源
     */
    @NotBlank(message = "积分来源不能为空")
    private String source;

    /**
     * 积分来源分类
     */
    private Integer sourceType;

    /**
     * 积分变动备注
     */
    private String remark;
}
