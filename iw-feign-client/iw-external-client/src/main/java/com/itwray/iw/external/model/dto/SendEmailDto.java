package com.itwray.iw.external.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SendEmailDto
 *
 * @author farui.wang
 * @since 2025/5/28
 */
@Data
public class SendEmailDto {

    /**
     * 发件人的发信地址
     */
    @NotBlank
    private String accountName;

    /**
     * 发件人别名
     */
    private String fromAlias;

    /**
     * 收件人地址
     */
    @NotBlank
    private String toAddress;

    /**
     * 邮件主题
     */
    @NotBlank
    private String subject;

    /**
     * 邮件正文的text内容
     * <p>textBody和htmlBody只需要使用其中之一</p>
     */
    private String textBody;

    /**
     * 邮件正文的html内容
     * <p>textBody和htmlBody只需要使用其中之一</p>
     * <p>htmlBody优先级大于textBody</p>
     */
    private String htmlBody;
}
