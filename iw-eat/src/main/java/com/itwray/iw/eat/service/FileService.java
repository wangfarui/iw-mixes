package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.vo.FileRecordVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口服务
 *
 * @author wray
 * @since 2024/5/11
 */
public interface FileService {

    FileRecordVo upload(MultipartFile file);
}
