package com.itwray.iw.points.model.param;

import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 查询过期任务
 *
 * @author wray
 * @since 2025/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryExpiredTaskParam extends PageDto {

    /**
     * 惩罚状态
     */
    private Integer punishStatus;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 截止日期
     */
    private LocalDate deadlineDate;
}
