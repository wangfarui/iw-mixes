package com.itwray.iw.web.model.vo;

import lombok.Data;

/**
 * 用户归属响应对象基类
 *
 * @author wray
 * @since 2026/3/19
 */
@Data
public abstract class AbstractUserOwnerVo implements UserOwnerAware {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 是否可编辑
     */
    private Boolean canEdit;
}
