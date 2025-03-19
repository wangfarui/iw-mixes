package com.itwray.iw.points.model.param;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

/**
 * 查询任务数量
 *
 * @author wray
 * @since 2025/3/19
 */
@Data
public class QueryTaskNumParam {

    /**
     * 分组id
     */
    @Nonnull
    private List<Integer> groupIds;

    /**
     * 任务状态 0-未完成 1-已完成 2-已放弃
     */
    @Nullable
    private Integer taskStatus;

    public QueryTaskNumParam(List<Integer> groupIds) {
        this.groupIds = groupIds;
    }
}
