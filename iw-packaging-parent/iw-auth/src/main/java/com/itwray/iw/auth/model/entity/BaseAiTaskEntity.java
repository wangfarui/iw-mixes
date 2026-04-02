package com.itwray.iw.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.auth.model.enums.AiTaskStatusEnum;
import com.itwray.iw.auth.model.enums.AiToolTypeEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * AI任务表
 *
 * @author wray
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_ai_task")
public class BaseAiTaskEntity extends UserEntity<Integer> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 会话任务描述
     */
    private String description;

    /**
     * 工具类型
     */
    private AiToolTypeEnum toolType;

    /**
     * sessionKey
     */
    private String sessionKey;

    /**
     * 任务状态
     */
    private AiTaskStatusEnum taskStatus;

    /**
     * 所属项目
     */
    private String projectName;

    /**
     * 工作区路径
     */
    private String workspacePath;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * git分支
     */
    private String gitBranch;

    /**
     * 记录文件路径
     */
    private String transcriptPath;

    /**
     * 恢复命令
     */
    private String resumeCommand;

    /**
     * 最近活跃时间
     */
    private LocalDateTime lastActiveAt;
}
