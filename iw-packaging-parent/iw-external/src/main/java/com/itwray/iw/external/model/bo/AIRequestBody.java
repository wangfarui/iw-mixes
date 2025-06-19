package com.itwray.iw.external.model.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * AI请求消息实体
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Data
public class AIRequestBody {

    /**
     * 对话的消息列表
     */
    private List<AIMessage> messages;

    /**
     * 使用的模型的 ID。您可以使用 deepseek-chat。
     * 可选值有: deepseek-chat, deepseek-reasoner
     */
    private String model;

    /**
     * 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性。
     * 默认0
     */
    private Integer frequency_penalty;

    /**
     * 默认2048
     */
    private Integer max_tokens;

    /**
     * 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，从而增加模型谈论新主题的可能性。
     * 默认0
     */
    private Integer presence_penalty;

    /**
     * 一个 object，指定模型必须输出的格式。
     */
    private AIResponseFormat response_format;

    /**
     * 一个 string 或最多包含 16 个 string 的 list，在遇到这些词时，API 将停止生成更多的 token。
     */
    private String stop;

    /**
     * 如果设置为 True，将会以 SSE（server-sent events）的形式以流式发送消息增量。消息流以 data: [DONE] 结尾。 默认false
     */
    private Boolean stream;

    /**
     * 流式输出相关选项。只有在 stream 参数为 true 时，才可设置此参数。
     */
    private AIStreamOptions stream_options;

    /**
     * 温度, 默认1.0, 区间 <=2
     */
    private BigDecimal temperature;

    /**
     * 采样温度, 默认1.0, 区间 <=1
     */
    private BigDecimal top_p;

    /**
     * 默认null
     */
    private Object tools;

    /**
     * 控制模型调用 tool 的行为。none 意味着模型不会调用任何 tool，而是生成一条消息。 默认none
     */
    private String tool_choice;

    /**
     * 是否返回所输出 token 的对数概率。如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。 默认false
     */
    private Boolean logprobs;

    /**
     * 一个介于 0 到 20 之间的整数 N，指定每个输出位置返回输出概率 top N 的 token，且返回这些 token 的对数概率。指定此参数时，logprobs 必须为 true。
     */
    private Integer top_logprobs;
}
