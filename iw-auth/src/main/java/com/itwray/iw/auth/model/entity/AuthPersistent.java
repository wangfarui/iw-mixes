package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证信息持久化表
 *
 * @author wray
 * @since 2024/3/20
 */
@TableName("iw_auth_persistent")
@Data
public class AuthPersistent {

    /**
     * 用户名
     */
    private String username;

    /**
     * 标识
     */
    @TableId
    private String series;

    /**
     * Token
     */
    private String token;

    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsed;
}
