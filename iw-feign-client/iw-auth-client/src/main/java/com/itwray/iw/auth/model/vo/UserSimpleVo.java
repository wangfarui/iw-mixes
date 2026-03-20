package com.itwray.iw.auth.model.vo;

import lombok.Data;

/**
 * 用户精简信息 VO
 *
 * @author wray
 * @since 2026/3/18
 */
@Data
public class UserSimpleVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户姓名
     */
    private String name;
}
