package com.itwray.iw.eat.service;

import com.itwray.iw.eat.model.vo.FileRecordVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件记录 接口服务层
 *
 * @author wray
 * @since 2024/5/17
 */
public interface BaseFileRecordService {

    /**
     * 上传文件并保存至文件记录
     *
     * @param file 文件对象
     * @return 文件记录
     */
    FileRecordVo uploadFile(MultipartFile file);
}
