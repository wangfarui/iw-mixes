package com.itwray.iw.generator;

/**
 * 代码生成类工具
 *
 * @author wray
 * @since 2025/4/21
 */
public class CodeGeneratorUtil {

    public static void main(String[] args) {
        GlobalConfig config = GlobalConfig.builder()
                .dbPassword("root")
                .outputDir("D:\\workspaces\\wray\\iw-mixes\\iw-code-generator")
                .parentPackage("com.itwray.iw.generator")
                .enabledWebModule(false)
                .enableFileOverride(false)
                .build();

        CodeGenerator.generate(config, "my_table");
    }
}
