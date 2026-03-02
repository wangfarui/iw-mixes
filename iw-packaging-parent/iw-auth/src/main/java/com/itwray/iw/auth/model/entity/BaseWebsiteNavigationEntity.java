package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.auth.model.enums.WebsiteNavigationStatusEnum;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站导航记录表
 *
 * @author wray
 * @since 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_website_navigation")
public class BaseWebsiteNavigationEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 网站链接
     */
    private String url;

    /**
     * 网站描述
     */
    private String description;

    /**
     * 网站图标URL
     */
    private String icon;

    /**
     * 网站分类
     */
    private String category;

    /**
     * 标签(JSON数组)
     */
    private String tags;

    /**
     * 网站状态
     *
     * @see WebsiteNavigationStatusEnum#getCode()
     */
    private WebsiteNavigationStatusEnum status;

    /**
     * 是否共享
     *
     * @see BoolEnum#getCode()
     */
    private Integer shared;
}
