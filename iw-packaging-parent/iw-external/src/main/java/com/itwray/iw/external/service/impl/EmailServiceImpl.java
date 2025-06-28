package com.itwray.iw.external.service.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dm20151123.AsyncClient;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailRequest;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailResponse;
import com.google.gson.Gson;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.external.model.dto.SendEmailDto;
import com.itwray.iw.external.service.EmailService;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.enums.RuntimeEnvironmentEnum;
import com.itwray.iw.web.utils.EnvironmentHolder;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 邮件服务 实现层
 *
 * @author farui.wang
 * @since 2025/5/28
 */
@Service
@Slf4j
@RefreshScope
public class EmailServiceImpl implements EmailService, ApplicationRunner {

    private AsyncClient client;

    /**
     * 运行环境
     */
    @Value("${iw.web.env:dev}")
    private RuntimeEnvironmentEnum env;

    @Override
    public GeneralResponse<Void> sendSingleEmail(SendEmailDto dto) {
        if (!RuntimeEnvironmentEnum.PROD.equals(env)) {
            log.info("非生产环境, 已跳过邮件发送流程");
            return GeneralResponse.success();
        }

        SingleSendMailRequest.Builder builder = SingleSendMailRequest.builder()
                .accountName(dto.getAccountName())
                .addressType(1)
                .replyToAddress(true)
                .toAddress(dto.getToAddress())
                .subject(dto.getSubject())
                .fromAlias(dto.getFromAlias());
        if (StringUtils.isNotBlank(dto.getHtmlBody())) {
            builder.htmlBody(dto.getHtmlBody());
        } else {
            builder.textBody(dto.getTextBody());
        }
        SingleSendMailRequest singleSendMailRequest = builder.build();

        try {
            CompletableFuture<SingleSendMailResponse> response = client.singleSendMail(singleSendMailRequest);
            SingleSendMailResponse resp = response.get();
            if (!resp.getStatusCode().equals(GeneralApiCode.SUCCESS.getCode())) {
                return GeneralResponse.fail("邮件发送失败，请重试");
            }
            log.info("sendEmail success: " + new Gson().toJson(resp));
        } catch (Exception e) {
            log.error("sendEmail error", e);
            throw new IwWebException("发送邮件异常");
        }
        return GeneralResponse.success();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(EnvironmentHolder.getProperty("aliyun.email.access-key-id"))
                .accessKeySecret(EnvironmentHolder.getProperty("aliyun.email.access-key-secret"))
                .build());

        client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dm.aliyuncs.com")
                )
                .build();
    }

    @PreDestroy
    public void destroy() {
        if (this.client != null) {
            this.client.close();
        }
    }
}
