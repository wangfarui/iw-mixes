package com.itwray.iw.external.service.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dm20151123.AsyncClient;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailRequest;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailResponse;
import com.google.gson.Gson;
import com.itwray.iw.external.model.dto.SendEmailDto;
import com.itwray.iw.external.service.EmailService;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.utils.EnvironmentHolder;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class EmailServiceImpl implements EmailService, ApplicationRunner {

    private AsyncClient client;

    @Override
    public void sendSingleEmail(SendEmailDto dto) {
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

        CompletableFuture<SingleSendMailResponse> response = client.singleSendMail(singleSendMailRequest);
        try {
            SingleSendMailResponse resp = response.get();
            log.info("sendEmail success: " + new Gson().toJson(resp));
        } catch (Exception e) {
            log.error("sendEmail error", e);
            throw new IwWebException("发送邮件异常");
        }
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
