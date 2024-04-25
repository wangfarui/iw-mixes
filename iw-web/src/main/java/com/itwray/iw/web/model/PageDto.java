package com.itwray.iw.web.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页请求对象
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public abstract class PageDto {

    /**
     * 当前页
     */
    @Min(value = 1, message = "当前页数不能小于1")
    private long currentPage = 1L;

    /**
     * 每页显示条数
     */
    @Min(value = 1, message = "每页显示条数不能小于1")
    private long pageSize = 10L;
}
