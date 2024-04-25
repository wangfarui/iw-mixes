package com.itwray.iw.web.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页响应对象
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class PageVo<T> {

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected long total = 0;

    public PageVo() {
    }

    public PageVo(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    public static <T> PageVo<T> of(List<T> records, long total) {
        return new PageVo<>(records, total);
    }
}
