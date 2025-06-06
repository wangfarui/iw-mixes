package com.itwray.iw.points.model.vo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计固定分组的待完成任务数量
 *
 * @author wray
 * @since 2025/3/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedGroupTaskNumVo {

    /**
     * 今日的任务数量
     */
    private Long todayNum;

    /**
     * 近一周的任务数量
     */
    private Long weekNum;

    /**
     * 无分组(收集箱)的任务数量
     */
    private Long noGroupNum;

    /**
     * 带有截止日期的任务数量
     */
    private Long withDeadlineNum;
}
