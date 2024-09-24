package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.auth.dao.BaseFileRecordDao;
import com.itwray.iw.auth.model.entity.BaseFileRecordEntity;
import com.itwray.iw.auth.service.BaseFileRecordService;
import com.itwray.iw.common.IwException;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.vo.FileRecordVo;
import com.itwray.iw.web.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件记录 服务实现层
 *
 * @author wray
 * @since 2024/5/17
 */
@Service
public class BaseFileRecordServiceImpl implements BaseFileRecordService {

    private final BaseFileRecordDao baseFileRecordDao;

    private final FileService fileService;

    @Autowired
    public BaseFileRecordServiceImpl(BaseFileRecordDao baseFileRecordDao, FileService fileService) {
        this.baseFileRecordDao = baseFileRecordDao;
        this.fileService = fileService;
    }

    /**
     * 上传文件并保存至文件记录
     *
     * @param file 文件对象
     * @return 文件记录
     */
    public FileRecordVo uploadFile(MultipartFile file) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            throw new IwWebException("文件不存在，请重试上传");
        }

        // 计算文件hash值
        String fileHash;
        try {
            fileHash = calculateFileHash(file.getInputStream());
        } catch (IOException e) {
            throw new IwWebException("文件数据异常");
        }

        // 查询文件是否存在
        byte[] fileHashByte = hexStringToByteArray(fileHash);
        BaseFileRecordEntity existEntity = baseFileRecordDao.lambdaQuery()
                .eq(BaseFileRecordEntity::getFileHash, fileHashByte)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (existEntity != null) {
            FileRecordVo fileRecordVo = BeanUtil.copyProperties(existEntity, FileRecordVo.class);
            fileRecordVo.setFileUrl(existEntity.getFilePrefix() + existEntity.getFileUri());
            return fileRecordVo;
        }

        // 上传文件至服务器
        FileRecordVo fileRecordVo = fileService.upload(file);

        // 保存文件上传记录
        BaseFileRecordEntity fileRecordEntity = BeanUtil.copyProperties(fileRecordVo, BaseFileRecordEntity.class);
        fileRecordEntity.setFileHash(fileHashByte);
        baseFileRecordDao.save(fileRecordEntity);

        // 返回文件记录
        fileRecordVo.setId(fileRecordEntity.getId());
        return fileRecordVo;
    }

    private static String calculateFileHash(InputStream inputStream) throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IwException(e);
        }
        try (DigestInputStream dis = new DigestInputStream(inputStream, digest)) {
            byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) {
                // continue reading
            }
        }

        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
