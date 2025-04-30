package com.itwray.iw.web.utils;

/**
 * 文件工具
 *
 * @author wray
 * @since 2025/4/23
 */
public abstract class FileUtils {

    public static boolean containHttpPrefix(String fileUrl) {
        return fileUrl.startsWith("http");
    }
}
