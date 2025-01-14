package com.itwray.iw.web.model.entity;

import java.io.Serializable;

/**
 * Id实体对象
 * <p>理论上，所有数据表实体对象都应该继承于这个对象!!!</p>
 *
 * @author wray
 * @since 2024/9/11
 */
public abstract class IdEntity<ID extends Serializable> {

    /**
     * 主键id的getter方法
     * <p>之所以不直接配置id变量，是为了应对id变量类型的不统一或id自动生成策略不一致的情况</p>
     *
     * @return 主键id
     */
    public abstract ID getId();
}
