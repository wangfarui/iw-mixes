package com.itwray.iw.eat.service;

import com.itwray.iw.web.model.FileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口服务
 *
 * @author wray
 * @since 2024/5/11
 */
public interface FileService {

    FileVo upload(MultipartFile file);
}
