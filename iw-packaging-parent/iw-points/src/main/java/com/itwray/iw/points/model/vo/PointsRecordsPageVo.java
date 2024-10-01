package com.itwray.iw.points.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录 分页VO
 *
 * @author wray
 * @since 2024/9/26
 */
@Data
public class PointsRecordsPageVo {

    private Integer id;

    /**
     * 积分变动类型(1表示增加, 2表示扣减)
     */
    private Integer transactionType;

    /**
     * 积分变动数量(可以是正数或负数)
     */
    private Integer points;

    /**
     * 积分来源
     */
    private String source;

    /**
     * 积分变动备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;
}
