package com.itwray.iw.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除对象
 * <p>可根据 DeleteDto 参数对象做条件判断，进行功能扩展</p>
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class DeleteDto {

    /**
     * 要删除对象的主键id
     */
    @NotNull(message = "id不能为空")
    private Number id;

    public Integer getIntId() {
        return id.intValue();
    }

    public Long getLongId() {
        return id.longValue();
    }
}
