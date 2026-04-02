package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI会话状态枚举
 *
 * @author wray
 * @since 2026-03-26
 */
@Getter
@AllArgsConstructor
public enum AiSessionStatusEnum implements BusinessConstantEnum {

    /**
     * 活跃中
     */
    ACTIVE(1, "活跃中"),

    /**
     * 待恢复
     */
    PENDING_RESUME(2, "待恢复"),

    /**
     * 已归档
     */
    ARCHIVED(3, "已归档");

    private final Integer code;
    private final String name;
}
