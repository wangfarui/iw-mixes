package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.service.BaseFileRecordsService;
import com.itwray.iw.web.model.vo.FileVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务 接口控制层
 *
 * @author wray
 * @since 2024/5/11
 */
@RestController
@RequestMapping("/file")
public class BaseFileController {

    @Resource
    private BaseFileRecordsService baseFileRecordsService;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public FileVo upload(@RequestParam("file") MultipartFile file) {
        return baseFileRecordsService.uploadFile(file);
    }
}
