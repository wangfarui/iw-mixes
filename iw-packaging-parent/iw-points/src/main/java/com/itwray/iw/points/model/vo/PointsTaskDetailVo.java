package com.itwray.iw.points.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.DetailVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分任务 详情VO
 *
 * @author wray
 * @since 2024/10/9
 */
@Data
public class PointsTaskDetailVo implements DetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务积分分数(可以是正数或负数)
     */
    private Integer taskPoints;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;
}
