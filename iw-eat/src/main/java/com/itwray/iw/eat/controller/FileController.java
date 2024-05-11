package com.itwray.iw.eat.controller;

import com.itwray.iw.eat.service.FileService;
import com.itwray.iw.web.model.FileVo;
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
public class FileController {

    @Resource
    private FileService fileService;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public FileVo upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }
}
