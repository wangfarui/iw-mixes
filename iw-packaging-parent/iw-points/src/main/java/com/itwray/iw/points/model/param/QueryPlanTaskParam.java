package com.itwray.iw.points.model.param;

import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 查询计划任务参数
 *
 * @author farui.wang
 * @since 2025/5/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPlanTaskParam extends PageDto {

    /**
     * 下一次计划生成日期
     */
    private LocalDate nextPlanDate;

    /**
     * 状态(0禁用 1启用)
     */
    private Integer status;
}
