package com.itwray.iw.web.model.vo;

import java.io.Serializable;

/**
 * web接口的详情VO
 *
 * @author wray
 * @since 2024/9/11
 */
public abstract class DetailVo {

    /**
     * 主键id的getter方法
     * <p>之所以不直接配置id变量，是为了应对id变量类型的不统一或id自动生成策略不一致的情况</p>
     *
     * @return 主键id
     */
    public abstract Serializable getId();
}
