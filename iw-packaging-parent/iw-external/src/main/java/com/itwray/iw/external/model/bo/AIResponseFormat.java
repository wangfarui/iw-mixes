package com.itwray.iw.external.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI响应格式对象
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseFormat {

    /**
     * 默认 text, 可选值只有 text or json_object
     * 设置为 { "type": "json_object" } 以启用 JSON 模式，该模式保证模型生成的消息是有效的 JSON
     */
    private String type;
}
