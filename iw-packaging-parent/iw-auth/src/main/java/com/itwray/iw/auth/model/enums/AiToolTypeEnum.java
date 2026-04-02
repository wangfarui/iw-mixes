package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI工具类型枚举
 *
 * @author wray
 * @since 2026-03-26
 */
@Getter
@AllArgsConstructor
public enum AiToolTypeEnum implements BusinessConstantEnum {

    /**
     * Codex
     */
    CODEX(1, "Codex"),

    /**
     * Claude Code
     */
    CLAUDE_CODE(2, "Claude Code"),

    /**
     * Gemini CLI
     */
    GEMINI_CLI(3, "Gemini CLI");

    private final Integer code;
    private final String name;
}
