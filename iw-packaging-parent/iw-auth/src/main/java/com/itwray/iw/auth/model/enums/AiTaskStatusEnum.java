package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI任务状态枚举
 *
 * @author wray
 * @since 2026-03-26
 */
@Getter
@AllArgsConstructor
public enum AiTaskStatusEnum implements BusinessConstantEnum {

    /**
     * 进行中
     */
    IN_PROGRESS(1, "进行中"),

    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),

    /**
     * 暂停
     */
    PAUSED(3, "暂停");

    private final Integer code;
    private final String name;
}
