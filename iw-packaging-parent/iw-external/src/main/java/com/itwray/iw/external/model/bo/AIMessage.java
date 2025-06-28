package com.itwray.iw.external.model.bo;

import lombok.Data;

/**
 * AI消息内容
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Data
public class AIMessage {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息发起的角色, 可选值有: system、user、assistant、tool
     */
    private String role;

    /**
     * 可以选填的参与者的名称，为模型提供信息以区分相同角色的参与者。 role=system、user、assistant 时使用
     */
    private String name;

    /**
     * (Beta) 设置此参数为 true，来强制模型在其回答中以此 assistant 消息中提供的前缀内容开始。 role=assistant 时使用
     */
    private Boolean prefix;

    /**
     * (Beta) 用于 deepseek-reasoner 模型在对话前缀续写功能下，作为最后一条 assistant 思维链内容的输入。使用此功能时，prefix 参数必须设置为 true。 role=assistant 时使用
     */
    private String reasoning_content;

    /**
     * 此消息所响应的 tool call 的 ID。 role=tool 时使用
     */
    private String tool_call_id;

    /**
     * 内部排序
     */
    private Long innerSort;

    public AIMessage() {
    }

    public AIMessage(String content, String role) {
        this.content = content;
        this.role = role;
    }
}
