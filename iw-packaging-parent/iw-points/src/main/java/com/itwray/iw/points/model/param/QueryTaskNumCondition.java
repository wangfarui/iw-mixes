package com.itwray.iw.points.model.param;

import lombok.Data;

import java.time.LocalDate;

/**
 * 查询任务数量条件对象
 *
 * @author wray
 * @since 2025/3/21
 */
@Data
public class QueryTaskNumCondition {

    /**
     * 任务状态 0-未完成 1-已完成 2-已放弃
     */
    private Integer taskStatus;

    /**
     * 截止日期(在重复任务中可被理解为开始日期)
     */
    private LocalDate deadlineDate;

    /**
     * 截止日期 开始时间
     */
    private LocalDate startDeadlineDate;

    /**
     * 截止日期 结束时间
     */
    private LocalDate endDeadlineDate;
}
