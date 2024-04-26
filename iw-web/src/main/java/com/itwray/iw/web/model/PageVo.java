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
     * 总数
     */
    protected long total = 0;

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    public PageVo() {
    }

    public PageVo( long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public static <T> PageVo<T> of(long total, List<T> records) {
        return new PageVo<>(total, records);
    }
}
