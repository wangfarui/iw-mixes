package com.itwray.iw.web.model.dto;

import lombok.Data;

/**
 * 文件请求对象
 *
 * @author wray
 * @since 2024/5/11
 */
@Data
public class FileDto {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件完整路径
     */
    private String fileUrl;
}
