package com.itwray.iw.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件响应对象
 *
 * @author wray
 * @since 2024/5/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件完整路径
     */
    private String fileUrl;
}
