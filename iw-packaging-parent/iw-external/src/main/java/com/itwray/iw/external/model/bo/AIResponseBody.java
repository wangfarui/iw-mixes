package com.itwray.iw.external.model.bo;

import lombok.Data;

import java.util.List;

/**
 * AI响应实体对象
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Data
public class AIResponseBody {

    /**
     * 该对话的唯一标识符。
     */
    private String id;

    /**
     * 对象的类型, 其值为 chat.completion。
     */
    private String object;

    /**
     * 创建聊天完成时的 Unix 时间戳（以秒为单位）。
     */
    private Long created;

    /**
     * 生成该 completion 的模型名。
     */
    private String model;

    /**
     * 模型生成的 completion 的选择列表。
     */
    private List<Choice> choices;

    /**
     * 该对话补全请求的用量信息。
     */
    private Usage usage;

    /**
     * This fingerprint represents the backend configuration that the model runs with.
     */
    private String system_fingerprint;

    @Data
    public static class Choice {

        /**
         * 该 completion 在模型生成的 completion 的选择列表中的索引。
         */
        private Integer index;

        /**
         * 模型生成的 completion 消息。
         */
        private AIMessage message;

        /**
         * 模型停止生成 token 的原因。
         * stop：模型自然停止生成，或遇到 stop 序列中列出的字符串。
         * length ：输出长度达到了模型上下文长度限制，或达到了 max_tokens 的限制。
         * content_filter：输出内容因触发过滤策略而被过滤。
         * insufficient_system_resource：系统推理资源不足，生成被打断。
         */
        private String finish_reason;
    }

    @Data
    public static class Usage {

        /**
         * 用户 prompt 所包含的 token 数。该值等于 prompt_cache_hit_tokens + prompt_cache_miss_tokens
         */
        private Integer prompt_tokens;

        /**
         * 模型 completion 产生的 token 数。
         */
        private Integer completion_tokens;

        /**
         * 该请求中，所有 token 的数量（prompt + completion）。
         */
        private Integer total_tokens;
    }
}
