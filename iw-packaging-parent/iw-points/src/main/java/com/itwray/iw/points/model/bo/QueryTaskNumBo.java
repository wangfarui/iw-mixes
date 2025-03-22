package com.itwray.iw.points.model.bo;

import lombok.Data;

/**
 * 查询任务数量
 *
 * @author wray
 * @since 2025/3/19
 */
@Data
public class QueryTaskNumBo {

    /**
     * 任务分组id
     */
    private Integer taskGroupId;

    /**
     * 任务数量
     */
    private Integer taskNum;
}
