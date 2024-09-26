package com.itwray.iw.web.model.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * web接口的编辑DTO
 *
 * @author wray
 * @since 2024/9/11
 */
public interface UpdateDto {

    /**
     * 主键id的getter方法
     * <p>之所以不直接配置id变量，是为了应对id变量类型的不统一或id自动生成策略不一致的情况</p>
     *
     * @return 主键id
     */
    @NotNull(message = "id不能为空")
    Serializable getId();
}
