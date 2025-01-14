package com.itwray.iw.points.model.dto;

import lombok.Data;

/**
 * 任务提交对象
 *
 * @author wray
 * @since 2025/1/14
 */
@Data
public class PointsTaskSubmitDto {

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 自定义积分
     */
    private Integer customPoints;
}
