package com.itwray.iw.web.mybatis;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.itwray.iw.common.constants.BoolEnums;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体对象
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
public abstract class BaseEntity {

    /**
     * 是否删除，默认false
     * <p>false -> 未删除</p>
     * <p>true -> 已删除</p>
     *
     * @see BoolEnums
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 主键id的getter方法
     * <p>之所以不直接配置id变量，是为了应对id变量类型的不统一或id自动生成策略不一致的情况</p>
     *
     * @return 主键id
     */
    public abstract Serializable getId();
}
