package com.itwray.iw.points.model.vo.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 积分任务 分页VO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
public class PointsTaskPlanPageVo {

    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划日期(开始日期)
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate planDate;

    /**
     * 下一次计划生成日期
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate nextPlanDate;

    /**
     * 状态(0禁用 1启用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;
}
