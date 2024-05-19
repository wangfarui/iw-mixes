package com.itwray.iw.eat.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.itwray.iw.eat.model.vo.FileRecordVo;
import com.itwray.iw.eat.service.FileService;
import com.itwray.iw.web.exception.IwWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务实现层
 *
 * @author wray
 * @since 2024/5/11
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${aliyun.oss.base-url}")
    private String ossBaseUrl;

    @Value("${aliyun.oss.endpoint}")
    private String ossEndpoint;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.upload.parent-dir}")
    private String uploadParentDir;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Override
    public FileRecordVo upload(MultipartFile file) {
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(this.ossEndpoint, this.accessKeyId, this.accessKeySecret);
            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            String uuid = UUID.randomUUID().toString();
            String fileSuffix = FileNameUtil.getSuffix(file.getOriginalFilename());
            String mainName = FileNameUtil.mainName(file.getOriginalFilename());
            String objectName = this.uploadParentDir + "/" + getNowDateDir() + "/" + mainName + "_" + uuid + "." + fileSuffix;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file.getInputStream());
            // 发起PutObject请求。
            ossClient.putObject(putObjectRequest);

            // 构建文件记录VO对象
            FileRecordVo fileRecordVo = new FileRecordVo();
            fileRecordVo.setFileName(file.getOriginalFilename());
            fileRecordVo.setFileUri("/" + objectName);
            fileRecordVo.setFilePrefix(this.ossBaseUrl);
            fileRecordVo.setFileSuffix(fileSuffix);
            fileRecordVo.setFileUrl(fileRecordVo.getFilePrefix() + fileRecordVo.getFileUri());
            return fileRecordVo;
        } catch (OSSException oe) {
            log.error("OSS文件上传客户端异常", oe);
            throw new IwWebException("文件上传异常");
        } catch (IOException ce) {
            log.error("OSS文件上传文件流异常", ce);
            throw new IwWebException("文件上传异常");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 获取当天日期的字符串
     *
     * @return yyyy-MM-dd
     */
    private String getNowDateDir() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
