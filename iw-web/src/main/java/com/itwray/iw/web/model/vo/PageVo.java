package com.itwray.iw.web.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页响应对象
 *
 * @author wray
 * @since 2024/4/24
 */
public class PageVo<T> implements IPage<T> {

    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

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

    @Override
    public List<OrderItem> orders() {
        return null;
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }
}
