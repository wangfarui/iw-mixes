package com.itwray.iw.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * 代码生成器的全局配置信息
 *
 * @author wray
 * @since 2024/11/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfig {

    /**
     * 数据库连接地址. e.g: jdbc:mysql://localhost:3306/db_name
     */
    private String dbUrl;

    /**
     * 数据库用户名
     */
    private String dbUsername;

    /**
     * 数据库密码
     */
    private String dbPassword;

    /**
     * 生成文件作者
     */
    private String author;

    /**
     * 生成文件的输出根目录
     */
    private String outputDir;

    /**
     * 父包名
     */
    private String parentPackage;

    public String getDbUrl() {
        return getIfAbsent(dbUrl, () -> "jdbc:mysql://localhost:3306/iw_mixes?serverTimezone=Asia/Shanghai");
    }

    public String getDbUsername() {
        return getIfAbsent(dbUsername, () -> "iw_root");
    }

    public String getDbPassword() {
        return getIfAbsent(dbPassword, () -> "password");
    }

    public String getAuthor() {
        return getIfAbsent(author, () -> "wray");
    }

    public String getParentPackage() {
        return getIfAbsent(parentPackage, () -> GlobalConfig.class.getPackage().getName() + ".temp");
    }

    public String getOutputDir() {
        return getIfAbsent(outputDir, () -> {
            // 使用当前类的资源路径
            String classPath = GlobalConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            // 获取子模块路径（向上返回到子模块根目录）
            Path modulePath = Paths.get(classPath).getParent().getParent();

            return modulePath.toString();
        });
    }

    public String getJavaDir() {
        return this.getOutputDir() + "/src/main/java";
    }

    public String getMapperDir() {
        return this.getOutputDir() + "/src/main/resources/mapper";
    }

    private String getIfAbsent(String value, Supplier<String> supplier) {
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return supplier.get();
    }
}
